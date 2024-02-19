package touch.baton.domain.runnerpost.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.SupporterRunnerPost;
import touch.baton.domain.member.command.repository.SupporterCommandRepository;
import touch.baton.domain.member.command.repository.SupporterRunnerPostCommandRepository;
import touch.baton.domain.member.command.vo.Message;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.event.RunnerPostApplySupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostAssignSupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostReviewStatusDoneEvent;
import touch.baton.domain.runnerpost.command.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.command.repository.RunnerPostCommandRepository;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;
import touch.baton.domain.tag.command.RunnerPostTag;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.repository.TagCommandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class RunnerPostCommandService {

    private final RunnerPostCommandRepository runnerPostCommandRepository;
    private final RunnerPostQueryRepository runnerPostQueryRepository;
    private final TagCommandRepository tagCommandRepository;
    private final SupporterCommandRepository supporterCommandRepository;
    private final SupporterRunnerPostCommandRepository supporterRunnerPostCommandRepository;
    private final ApplicationEventPublisher eventPublisher;

    public Long createRunnerPost(final Runner runner, final RunnerPostCreateRequest request) {
        final RunnerPost runnerPost = toDomain(runner, request);
        runnerPostCommandRepository.save(runnerPost);

        final List<Tag> tags = findTagsAfterSave(request.tags());

        final List<RunnerPostTag> runnerPostTags = tags.stream()
                .map(tag -> RunnerPostTag.builder()
                        .tag(tag)
                        .runnerPost(runnerPost).build())
                .toList();

        runnerPost.addAllRunnerPostTags(runnerPostTags);
        return runnerPost.getId();
    }

    private RunnerPost toDomain(final Runner runner, final RunnerPostCreateRequest request) {
        return RunnerPost.newInstance(request.title(),
                request.implementedContents(),
                request.curiousContents(),
                request.postscriptContents(),
                request.pullRequestUrl(),
                request.deadline(),
                runner);
    }

    private List<Tag> findTagsAfterSave(final List<String> tagNames) {
        final List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tagCommandRepository.findByTagName(new TagName(tagName))
                    .ifPresentOrElse(tags::add, addTagAfterSave(tags, tagName));
        }

        return tags;
    }

    private Runnable addTagAfterSave(final List<Tag> tags, final String tagName) {
        return () -> {
            final Tag savedTag = tagCommandRepository.save(Tag.newInstance(tagName));
            tags.add(savedTag);
        };
    }

    public void deleteByRunnerPostId(final Long runnerPostId, final Runner runner) {
        final RunnerPost runnerPost = runnerPostCommandRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 삭제할 러너 게시글이 존재하지 않습니다."));
        if (runnerPost.isNotOwner(runner)) {
            throw new RunnerPostBusinessException("RunnerPost 를 게시한 유저가 아닙니다.");
        }
        if (runnerPost.isReviewStatusStarted()) {
            throw new RunnerPostBusinessException("삭제할 수 없는 상태의 리뷰 상태입니다.");
        }
        if (supporterRunnerPostCommandRepository.existsByRunnerPostId(runnerPostId)) {
            throw new RunnerPostBusinessException("지원자가 존재하여 삭제할 수 없습니다.");
        }
        runnerPostCommandRepository.deleteById(runnerPostId);
    }

    private RunnerPost getRunnerPostOrThrowException(final Long runnerPostId) {
        return runnerPostCommandRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("해당 runnerPostId 로 러너 게시글을 찾을 수 없습니다. runnerPostId 를 다시 확인해주세요"));
    }

    public Long createRunnerPostApplicant(final Supporter supporter,
                                          final RunnerPostApplicantCreateRequest request,
                                          final Long runnerPostId
    ) {
        final RunnerPost foundRunnerPost = getRunnerPostOrThrowException(runnerPostId);
        if (isApplySupporter(foundRunnerPost, supporter)) {
            throw new RunnerPostBusinessException("Supporter 는 이미 해당 RunnerPost 에 리뷰 신청을 한 이력이 있습니다.");
        }

        final SupporterRunnerPost runnerPostApplicant = SupporterRunnerPost.builder()
                .supporter(supporter)
                .runnerPost(foundRunnerPost)
                .message(new Message(request.message()))
                .build();

        final Long savedApplicantId = supporterRunnerPostCommandRepository.save(runnerPostApplicant).getId();

        eventPublisher.publishEvent(new RunnerPostApplySupporterEvent(foundRunnerPost.getId()));

        return savedApplicantId;
    }

    public void updateRunnerPostReviewStatusDone(final Long runnerPostId, final Supporter supporter) {
        final RunnerPost foundRunnerPost = runnerPostQueryRepository.joinSupporterByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("해당 식별자의 러너 게시글이 존재하지 않습니다."));

        if (Objects.isNull(foundRunnerPost.getSupporter())) {
            throw new RunnerPostBusinessException("아직 서포터가 배정이 안 된 게시글 입니다.");
        }

        if (foundRunnerPost.isDifferentSupporter(supporter)) {
            throw new RunnerPostBusinessException("다른 사람이 리뷰 중인 게시글의 상태를 변경할 수 없습니다.");
        }

        foundRunnerPost.finishReview();

        eventPublisher.publishEvent(new RunnerPostReviewStatusDoneEvent(foundRunnerPost.getId()));
    }

    public void deleteSupporterRunnerPost(final Supporter supporter, final Long runnerPostId) {
        final RunnerPost runnerPost = runnerPostCommandRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("존재하지 않는 RunnerPost 입니다."));
        if (!runnerPost.isReviewStatusNotStarted()) {
            throw new RunnerPostBusinessException("이미 진행 중인 러너 게시글의 서포터 지원은 철회할 수 없습니다.");
        }
        supporterRunnerPostCommandRepository.deleteBySupporterIdAndRunnerPostId(supporter.getId(), runnerPostId);
    }

    public void updateRunnerPostAppliedSupporter(final Runner runner,
                                                 final Long runnerPostId,
                                                 final RunnerPostUpdateRequest.SelectSupporter request
    ) {
        final Supporter foundApplySupporter = supporterCommandRepository.findById(request.supporterId())
                .orElseThrow(() -> new RunnerPostBusinessException("해당하는 식별자값의 서포터를 찾을 수 없습니다."));
        final RunnerPost foundRunnerPost = runnerPostCommandRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다."));

        if (!isApplySupporter(foundRunnerPost, foundApplySupporter)) {
            throw new RunnerPostBusinessException("게시글에 리뷰를 제안한 서포터가 아닙니다.");
        }
        if (foundRunnerPost.isNotOwner(runner)) {
            throw new RunnerPostBusinessException("RunnerPost 의 글쓴이와 다른 사용자입니다.");
        }

        foundRunnerPost.assignSupporter(foundApplySupporter);

        eventPublisher.publishEvent(new RunnerPostAssignSupporterEvent(foundRunnerPost.getId()));
    }

    private boolean isApplySupporter(final RunnerPost runnerPost, final Supporter supporter) {
        return supporterRunnerPostCommandRepository.existsByRunnerPostIdAndSupporterId(runnerPost.getId(), supporter.getId());
    }
}
