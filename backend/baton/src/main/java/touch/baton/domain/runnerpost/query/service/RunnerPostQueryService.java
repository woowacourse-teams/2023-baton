package touch.baton.domain.runnerpost.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.request.PageParams;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.common.vo.Page;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.SupporterRunnerPost;
import touch.baton.domain.member.query.repository.SupporterRunnerPostQueryRepository;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.RunnerPostsApplicantCount;
import touch.baton.domain.runnerpost.command.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.command.repository.dto.RunnerPostApplicantCountDto;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.query.repository.RunnerPostPageRepository;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;
import touch.baton.domain.tag.command.RunnerPostTag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.domain.tag.query.repository.RunnerPostTagQueryRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostQueryService {

    private final RunnerPostQueryRepository runnerPostQueryRepository;
    private final RunnerPostPageRepository runnerPostPageRepository;
    private final RunnerPostTagQueryRepository runnerPostTagQueryRepository;
    private final SupporterRunnerPostQueryRepository supporterRunnerPostQueryRepository;

    public RunnerPost readByRunnerPostId(final Long runnerPostId) {
        runnerPostTagQueryRepository.joinTagByRunnerPostId(runnerPostId);
        return runnerPostQueryRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다."));
    }

    public PageResponse<RunnerPostResponse.Simple> pageRunnerPostByTagNameAndReviewStatus(final String tagName,
                                                                                          final PageParams pageParams,
                                                                                          final ReviewStatus reviewStatus
    ) {
        final Page<RunnerPost> runnerPosts = runnerPostPageRepository.pageByReviewStatusAndTagReducedName(pageParams.cursor(), pageParams.getLimitForQuery(), TagReducedName.nullableInstance(tagName), reviewStatus);
        final List<RunnerPostTag> runnerPostTags = runnerPostPageRepository.findRunnerPostTagsByRunnerPosts(runnerPosts.contents());
        final RunnerPostsApplicantCount runnerPostsApplicantCount = readRunnerPostsApplicantCount(runnerPosts.contents());
        final List<RunnerPostResponse.Simple> responses = runnerPosts.contents().stream()
                .map(runnerPost -> RunnerPostResponse.Simple.of(runnerPost, runnerPostsApplicantCount.getApplicantCountById(runnerPost.getId()), runnerPostTags))
                .toList();
        return PageResponse.of(responses, pageParams, runnerPosts.total());
    }

    public PageResponse<RunnerPostResponse.Simple> pageRunnerPostBySupporterIdAndReviewStatus(final PageParams pageParams,
                                                                                              final Long supporterId,
                                                                                              final ReviewStatus reviewStatus
    ) {
        final List<RunnerPost> runnerPosts = pageRunnerPostFromSupporterByReviewStatus(pageParams, supporterId, reviewStatus);
        final List<RunnerPostTag> runnerPostTags = runnerPostPageRepository.findRunnerPostTagsByRunnerPosts(runnerPosts);
        final RunnerPostsApplicantCount runnerPostsApplicantCount = readRunnerPostsApplicantCount(runnerPosts);
        final List<RunnerPostResponse.Simple> responses = runnerPosts.stream()
                .map(runnerPost -> RunnerPostResponse.Simple.of(runnerPost, runnerPostsApplicantCount.getApplicantCountById(runnerPost.getId()), runnerPostTags))
                .toList();
        return PageResponse.of(responses, pageParams);
    }

    private List<RunnerPost> pageRunnerPostFromSupporterByReviewStatus(final PageParams pageParams,
                                                                       final Long supporterId,
                                                                       final ReviewStatus reviewStatus
    ) {
        if (reviewStatus == ReviewStatus.NOT_STARTED) {
            return runnerPostPageRepository.pageBySupporterIdAndReviewStatusNotStarted(pageParams.cursor(), pageParams.getLimitForQuery(), supporterId);
        }
        return runnerPostPageRepository.pageBySupporterIdAndReviewStatus(pageParams.cursor(), pageParams.getLimitForQuery(), supporterId, reviewStatus);
    }

    private RunnerPostsApplicantCount readRunnerPostsApplicantCount(final List<RunnerPost> runnerPosts) {
        final List<Long> runnerPostIds = runnerPosts.stream()
                .map(RunnerPost::getId)
                .toList();
        final List<RunnerPostApplicantCountDto> runnerPostApplicantCountDtos = runnerPostQueryRepository.countApplicantsByRunnerPostIds(runnerPostIds);
        return RunnerPostsApplicantCount.from(runnerPostApplicantCountDtos);
    }

    public PageResponse<RunnerPostResponse.SimpleByRunner> pageRunnerPostByRunnerIdAndReviewStatus(final PageParams pageParams,
                                                                                                   final Long runnerId,
                                                                                                   final ReviewStatus reviewStatus
    ) {
        final List<RunnerPost> runnerPosts = runnerPostPageRepository.pageByRunnerIdAndReviewStatus(pageParams.cursor(), pageParams.getLimitForQuery(), runnerId, reviewStatus);
        final List<RunnerPostTag> runnerPostTags = runnerPostPageRepository.findRunnerPostTagsByRunnerPosts(runnerPosts);
        final RunnerPostsApplicantCount runnerPostsApplicantCount = readRunnerPostsApplicantCount(runnerPosts);
        final List<RunnerPostResponse.SimpleByRunner> responses = runnerPosts.stream()
                .map(runnerPost -> RunnerPostResponse.SimpleByRunner.of(runnerPost, runnerPostsApplicantCount.getApplicantCountById(runnerPost.getId()), runnerPostTags))
                .toList();
        return PageResponse.of(responses, pageParams);
    }

    public List<SupporterRunnerPost> readSupporterRunnerPostsByRunnerPostId(final Runner runner, final Long runnerPostId) {
        final RunnerPost foundRunnerPost = runnerPostQueryRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException(("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다.")));

        if (foundRunnerPost.isNotOwner(runner)) {
            throw new RunnerPostBusinessException("RunnerPost 의 작성자가 아닙니다.");
        }

        return supporterRunnerPostQueryRepository.readByRunnerPostId(runnerPostId);
    }

    public List<RunnerPost> readRunnerPostsByRunnerId(final Long runnerId) {
        return runnerPostQueryRepository.findByRunnerId(runnerId);
    }

    public boolean existsRunnerPostApplicantByRunnerPostIdAndMemberId(final Long runnerPostId, final Long memberId) {
        return supporterRunnerPostQueryRepository.existsByRunnerPostIdAndMemberId(runnerPostId, memberId);
    }

    public long countApplicantsByRunnerPostId(final Long runnerPostId) {
        return supporterRunnerPostQueryRepository.countByRunnerPostId(runnerPostId).orElse(0L);
    }

    public long countRunnerPostByRunnerIdAndReviewStatus(final Long runnerId, final ReviewStatus reviewStatus) {
        return runnerPostQueryRepository.countByRunnerIdAndReviewStatus(runnerId, reviewStatus);
    }

    public long countRunnerPostBySupporterIdAndReviewStatus(final Long supporterId, final ReviewStatus reviewStatus) {
        if (reviewStatus.isNotStarted()) {
            return supporterRunnerPostQueryRepository.countRunnerPostBySupporterIdByReviewStatusNotStarted(supporterId);
        }
        return runnerPostQueryRepository.countBySupporterIdAndReviewStatus(supporterId, reviewStatus);
    }

    @Transactional
    public void increaseWatchedCount(final RunnerPost runnerPost) {
        runnerPost.increaseWatchedCount();
    }
}
