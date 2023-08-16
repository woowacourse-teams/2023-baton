package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateTestRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
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
import java.util.Optional;

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

    private RunnerPost toDomain(final Runner runner, final RunnerPostCreateRequest request) {
        return RunnerPost.newInstance(request.title(),
                request.contents(),
                request.pullRequestUrl(),
                request.deadline(),
                runner);
    }

    @Transactional
    public Long createRunnerPostTest(final Runner runner, final RunnerPostCreateTestRequest request) {
        final RunnerPost runnerPost = RunnerPost.newInstance(request.title(),
                request.contents(),
                request.pullRequestUrl(),
                request.deadline(),
                runner);

        if (Objects.nonNull(request.supporterId())) {
            final Supporter supporter = supporterRepository.findById(request.supporterId())
                    .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 서포터가 존재하지 않습니다."));
            runnerPost.assignSupporter(supporter);
        }

        runnerPostRepository.save(runnerPost);

        List<Tag> toSaveTags = new ArrayList<>();
        for (final String tagName : request.tags()) {
            final Optional<Tag> maybeTag = tagRepository.findByTagName(new TagName(tagName));

            if (maybeTag.isEmpty()) {
                final Tag savedTag = tagRepository.save(Tag.newInstance(tagName));
                toSaveTags.add(savedTag);
                continue;
            }

            final Tag presentTag = maybeTag.get();
            toSaveTags.add(presentTag);
        }

        final List<RunnerPostTag> postTags = toSaveTags.stream()
                .map(tag -> RunnerPostTag.builder()
                        .tag(tag)
                        .runnerPost(runnerPost)
                        .build())
                .toList();

        runnerPost.addAllRunnerPostTags(postTags);
        return runnerPost.getId();
    }

    public RunnerPost readByRunnerPostId(final Long runnerPostId) {
        runnerPostTagRepository.joinTagByRunnerPostId(runnerPostId);
        return runnerPostRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다."));
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

    @Transactional
    public Long updateRunnerPost(final Long runnerPostId, final Runner runner, final RunnerPostUpdateRequest.Default request) {
        // TODO: 메소드 분리
        // FIXME: 2023/08/03 주인 확인 로직 넣기
        final RunnerPost runnerPost = getRunnerPostOrThrowException(runnerPostId);
        runnerPost.updateTitle(new Title(request.title()));
        runnerPost.updateContents(new Contents(request.contents()));
        runnerPost.updatePullRequestUrl(new PullRequestUrl(request.pullRequestUrl()));
        runnerPost.updateDeadLine(new Deadline(request.deadline()));

        final List<RunnerPostTag> presentRunnerPostTags = runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());
        // TODO: tag 개수 차감 메소드 분리
        final List<touch.baton.domain.tag.Tag> presentTags = presentRunnerPostTags.stream()
                .map(RunnerPostTag::getTag)
                .toList();

        // TODO: 새로운 tag 로 교체 메소드 분리
        final List<RunnerPostTag> removedRunnerPostTags = new ArrayList<>(presentRunnerPostTags);
        for (String tagName : request.tags()) {
            final Optional<RunnerPostTag> existRunnerPostTag = presentRunnerPostTags.stream()
                    .filter(presentRunnerPostTag -> presentRunnerPostTag.isSameTagName(tagName))
                    .findFirst();
            if (existRunnerPostTag.isPresent()) {
                removedRunnerPostTags.remove(existRunnerPostTag.get());
            }
            if (existRunnerPostTag.isEmpty()) {
                // TODO: tag 찾기 메소드 분리
                final Optional<Tag> tag = tagRepository.findByTagName(new TagName(tagName));
                if (tag.isEmpty()) {
                    final Tag newTag = tagRepository.save(Tag.newInstance(tagName));
                    final RunnerPostTag newRunnerPostTag = runnerPostTagRepository.save(RunnerPostTag.builder()
                            .runnerPost(runnerPost)
                            .tag(newTag)
                            .build());
                    runnerPost.appendRunnerPostTag(newRunnerPostTag);
                }
                if (tag.isPresent()) {
                    final RunnerPostTag newRunnerPostTag = runnerPostTagRepository.save(RunnerPostTag.builder()
                            .runnerPost(runnerPost)
                            .tag(tag.get())
                            .build());
                    runnerPost.appendRunnerPostTag(newRunnerPostTag);
                }
            }
        }
        runnerPostTagRepository.deleteAll(removedRunnerPostTags);

        return runnerPost.getId();
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

    public Page<RunnerPost> readAllRunnerPosts(final Pageable pageable) {
        return runnerPostRepository.findAll(pageable);
    }

    public List<RunnerPost> readRunnerPostsByRunnerId(final Long runnerId) {
        return runnerPostRepository.findByRunnerId(runnerId);
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
        final List<Long> applicantCounts = supporterRunnerPostRepository.countByRunnerPostIdIn(runnerPostIds);
        if (applicantCounts.isEmpty()) {
            initApplicantCounts(runnerPostIds, applicantCounts);
        }

        return applicantCounts;
    }

    private void initApplicantCounts(final List<Long> runnerPostIds, final List<Long> applicantCounts) {
        for (int i = 0; i < runnerPostIds.size(); i++) {
            applicantCounts.add(0L);
        }
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
        return supporterRunnerPostRepository.countByRunnerPostId(runnerPostId).orElseGet(() -> 0);
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
