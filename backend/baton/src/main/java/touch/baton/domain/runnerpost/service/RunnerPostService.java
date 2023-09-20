package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.repository.SupporterRunnerPostRepository;
import touch.baton.domain.supporter.vo.Message;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostService {

    private final RunnerPostRepository runnerPostRepository;
    private final RunnerPostTagRepository runnerPostTagRepository;
    private final TagRepository tagRepository;
    private final SupporterRepository supporterRepository;
    private final SupporterRunnerPostRepository supporterRunnerPostRepository;

    @Transactional
    public Long createRunnerPost(final Runner runner, final RunnerPostCreateRequest request) {
        final RunnerPost runnerPost = toDomain(runner, request);
        runnerPostRepository.save(runnerPost);

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
            tagRepository.findByTagName(new TagName(tagName))
                    .ifPresentOrElse(tags::add, addTagAfterSave(tags, tagName));
        }

        return tags;
    }

    private Runnable addTagAfterSave(final List<Tag> tags, final String tagName) {
        return () -> {
            final Tag savedTag = tagRepository.save(Tag.newInstance(tagName));
            tags.add(savedTag);
        };
    }

    public RunnerPost readByRunnerPostId(final Long runnerPostId) {
        runnerPostTagRepository.joinTagByRunnerPostId(runnerPostId);
        return runnerPostRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다."));
    }

    public List<SupporterRunnerPost> readSupporterRunnerPostsByRunnerPostId(final Runner runner, final Long runnerPostId) {
        final RunnerPost foundRunnerPost = runnerPostRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException(("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다.")));

        if (foundRunnerPost.isNotOwner(runner)) {
            throw new RunnerPostBusinessException("RunnerPost 의 작성자가 아닙니다.");
        }

        return supporterRunnerPostRepository.readByRunnerPostId(runnerPostId);
    }

    @Transactional
    public void increaseWatchedCount(final RunnerPost runnerPost) {
        runnerPost.increaseWatchedCount();
    }

    @Transactional
    public void deleteByRunnerPostId(final Long runnerPostId, final Runner runner) {
        final RunnerPost runnerPost = runnerPostRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 삭제할 러너 게시글이 존재하지 않습니다."));
        if (runnerPost.isNotOwner(runner)) {
            throw new RunnerPostBusinessException("RunnerPost 를 게시한 유저가 아닙니다.");
        }
        if (runnerPost.isReviewStatusStarted()) {
            throw new RunnerPostBusinessException("삭제할 수 없는 상태의 리뷰 상태입니다.");
        }
        if (supporterRunnerPostRepository.existsByRunnerPostId(runnerPostId)) {
            throw new RunnerPostBusinessException("지원자가 존재하여 삭제할 수 없습니다.");
        }
        runnerPostRepository.deleteById(runnerPostId);
    }

    private RunnerPost getRunnerPostOrThrowException(final Long runnerPostId) {
        return runnerPostRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("해당 runnerPostId 로 러너 게시글을 찾을 수 없습니다. runnerPostId 를 다시 확인해주세요"));
    }

    @Transactional
    public Long createRunnerPostApplicant(final Supporter supporter,
                                          final RunnerPostApplicantCreateRequest request,
                                          final Long runnerPostId
    ) {
        final RunnerPost foundRunnerPost = getRunnerPostOrThrowException(runnerPostId);
        final boolean isApplicantHistoryExist = supporterRunnerPostRepository.existsByRunnerPostIdAndSupporterId(runnerPostId, supporter.getId());
        if (isApplicantHistoryExist) {
            throw new RunnerPostBusinessException("Supporter 는 이미 해당 RunnerPost 에 리뷰 신청을 한 이력이 있습니다.");
        }

        final SupporterRunnerPost runnerPostApplicant = SupporterRunnerPost.builder()
                .supporter(supporter)
                .runnerPost(foundRunnerPost)
                .message(new Message(request.message()))
                .build();

        return supporterRunnerPostRepository.save(runnerPostApplicant).getId();
    }

    public Page<RunnerPost> readRunnerPostsByReviewStatus(final Pageable pageable, final ReviewStatus reviewStatus) {
        return runnerPostRepository.findByReviewStatus(pageable, reviewStatus);
    }

    public List<RunnerPost> readRunnerPostsByRunnerId(final Long runnerId) {
        return runnerPostRepository.findByRunnerId(runnerId);
    }

    public Page<RunnerPost> readRunnerPostsByRunnerIdAndReviewStatus(final Pageable pageable,
                                                                     final Long runnerId,
                                                                     final ReviewStatus reviewStatus
    ) {
        return runnerPostRepository.findByRunnerIdAndReviewStatus(pageable, runnerId, reviewStatus);
    }

    public Page<RunnerPost> readRunnerPostsBySupporterIdAndReviewStatus(final Pageable pageable,
                                                                        final Long supporterId,
                                                                        final ReviewStatus reviewStatus
    ) {
        if (reviewStatus.isSameAsNotStarted()) {
            return runnerPostRepository.joinSupporterRunnerPostBySupporterIdAndReviewStatus(pageable, supporterId, reviewStatus);
        }
        return runnerPostRepository.findBySupporterIdAndReviewStatus(pageable, supporterId, reviewStatus);
    }

    public List<Long> readCountsByRunnerPostIds(final List<Long> runnerPostIds) {
        return supporterRunnerPostRepository.countByRunnerPostIds(runnerPostIds);
    }

    @Transactional
    public void updateRunnerPostReviewStatusDone(final Long runnerPostId, final Supporter supporter) {
        final RunnerPost foundRunnerPost = runnerPostRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("해당 식별자의 러너 게시글이 존재하지 않습니다."));

        if (Objects.isNull(foundRunnerPost.getSupporter())) {
            throw new RunnerPostBusinessException("아직 서포터가 배정이 안 된 게시글 입니다.");
        }

        if (foundRunnerPost.isDifferentSupporter(supporter)) {
            throw new RunnerPostBusinessException("다른 사람이 리뷰 중인 게시글의 상태를 변경할 수 없습니다.");
        }

        foundRunnerPost.finishReview();
    }

    public long readCountByRunnerPostId(final Long runnerPostId) {
        return supporterRunnerPostRepository.countByRunnerPostId(runnerPostId).orElse(0L);
    }

    @Transactional
    public void deleteSupporterRunnerPost(final Supporter supporter, final Long runnerPostId) {
        final RunnerPost runnerPost = runnerPostRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("존재하지 않는 RunnerPost 입니다."));
        if (!runnerPost.isReviewStatusNotStarted()) {
            throw new RunnerPostBusinessException("이미 진행 중인 러너 게시글의 서포터 지원은 철회할 수 없습니다.");
        }
        supporterRunnerPostRepository.deleteBySupporterIdAndRunnerPostId(supporter.getId(), runnerPostId);
    }

    @Transactional
    public void updateRunnerPostAppliedSupporter(final Runner runner,
                                                 final Long runnerPostId,
                                                 final RunnerPostUpdateRequest.SelectSupporter request
    ) {
        final Supporter foundApplySupporter = supporterRepository.findById(request.supporterId())
                .orElseThrow(() -> new RunnerPostBusinessException("해당하는 식별자값의 서포터를 찾을 수 없습니다."));
        final RunnerPost foundRunnerPost = runnerPostRepository.findById(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다."));

        if (isApplySupporter(runnerPostId, foundApplySupporter)) {
            throw new RunnerPostBusinessException("게시글에 리뷰를 제안한 서포터가 아닙니다.");
        }
        if (foundRunnerPost.isNotOwner(runner)) {
            throw new RunnerPostBusinessException("RunnerPost 의 글쓴이와 다른 사용자입니다.");
        }

        foundRunnerPost.assignSupporter(foundApplySupporter);
    }

    private boolean isApplySupporter(final Long runnerPostId, final Supporter foundSupporter) {
        return !supporterRunnerPostRepository.existsByRunnerPostIdAndSupporterId(runnerPostId, foundSupporter.getId());
    }

    public boolean existsRunnerPostApplicantByRunnerPostIdAndMemberId(final Long runnerPostId, final Long memberId) {
        return supporterRunnerPostRepository.existsByRunnerPostIdAndMemberId(runnerPostId, memberId);
    }
}
