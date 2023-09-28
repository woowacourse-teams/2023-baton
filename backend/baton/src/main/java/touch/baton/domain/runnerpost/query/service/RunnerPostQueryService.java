package touch.baton.domain.runnerpost.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.SupporterRunnerPost;
import touch.baton.domain.member.query.controller.response.RunnerResponse;
import touch.baton.domain.member.query.repository.SupporterRunnerPostQueryRepository;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.RunnerPostsApplicantCount;
import touch.baton.domain.runnerpost.command.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.command.controller.response.RunnerPostResponses;
import touch.baton.domain.runnerpost.command.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.command.repository.dto.RunnerPostApplicantCountDto;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;
import touch.baton.domain.runnerpost.query.service.dto.PageParams;
import touch.baton.domain.tag.command.RunnerPostTag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.domain.tag.query.repository.RunnerPostTagQueryRepository;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostQueryService {

    private final RunnerPostQueryRepository runnerPostQueryRepository;
    private final RunnerPostTagQueryRepository runnerPostTagQueryRepository;
    private final SupporterRunnerPostQueryRepository supporterRunnerPostQueryRepository;

    public RunnerPostResponses.Simple readRunnerPostByPageInfoAndTagNameAndReviewStatus(final String tagName,
                                                                                        final PageParams pageParams,
                                                                                        final ReviewStatus reviewStatus
    ) {
        final List<RunnerPost> runnerPosts = runnerPostQueryRepository.pageByReviewStatusAndTagReducedName(pageParams.cursor(), pageParams.limit(), TagReducedName.nullableInstance(tagName), reviewStatus);
        final List<RunnerPostTag> runnerPostTags = runnerPostQueryRepository.findRunnerPostTagsByRunnerPosts(runnerPosts);
        final RunnerPostsApplicantCount runnerPostsApplicantCount = readRunnerPostsApplicantCount(runnerPosts);
        return convertToSimpleResponses(runnerPosts, runnerPostTags, runnerPostsApplicantCount);
    }

    private RunnerPostsApplicantCount readRunnerPostsApplicantCount(final List<RunnerPost> runnerPosts) {
        final List<Long> runnerPostIds = runnerPosts.stream()
                .map(RunnerPost::getId)
                .toList();
        final List<RunnerPostApplicantCountDto> runnerPostApplicantCountDtos = runnerPostQueryRepository.countApplicantsByRunnerPostIds(runnerPostIds);
        return RunnerPostsApplicantCount.from(runnerPostApplicantCountDtos);
    }

    private RunnerPostResponses.Simple convertToSimpleResponses(final List<RunnerPost> runnerPosts,
                                                                final List<RunnerPostTag> runnerPostTags,
                                                                final RunnerPostsApplicantCount runnerPostsApplicantCount
    ) {
        final List<RunnerPostResponse.Simple> responses = runnerPosts.stream()
                .map(runnerPost -> convertToSimpleResponse(runnerPost, runnerPostsApplicantCount.getApplicantCountById(runnerPost.getId()), runnerPostTags))
                .toList();
        return RunnerPostResponses.Simple.from(responses);
    }

    private RunnerPostResponse.Simple convertToSimpleResponse(final RunnerPost runnerPost,
                                                              final long applicantCount,
                                                              final List<RunnerPostTag> runnerPostTags
    ) {
        return new RunnerPostResponse.Simple(
                runnerPost.getId(),
                runnerPost.getTitle().getValue(),
                runnerPost.getDeadline().getValue(),
                runnerPost.getWatchedCount().getValue(),
                applicantCount,
                runnerPost.getReviewStatus().name(),
                RunnerResponse.Simple.from(runnerPost.getRunner()),
                convertToTags(runnerPost, runnerPostTags)
        );
    }

    private List<String> convertToTags(final RunnerPost runnerPost, final List<RunnerPostTag> runnerPostTags) {
        return runnerPostTags.stream()
                .filter(runnerPostTag -> Objects.equals(runnerPostTag.getRunnerPost().getId(), runnerPost.getId()))
                .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                .toList();
    }

    public RunnerPost readByRunnerPostId(final Long runnerPostId) {
        runnerPostTagQueryRepository.joinTagByRunnerPostId(runnerPostId);
        return runnerPostQueryRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다."));
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

    public Page<RunnerPost> readRunnerPostsByRunnerIdAndReviewStatus(final Pageable pageable,
                                                                     final Long runnerId,
                                                                     final ReviewStatus reviewStatus
    ) {
        return runnerPostQueryRepository.findByRunnerIdAndReviewStatus(pageable, runnerId, reviewStatus);
    }

    public Page<RunnerPost> readRunnerPostsBySupporterIdAndReviewStatus(final Pageable pageable,
                                                                        final Long supporterId,
                                                                        final ReviewStatus reviewStatus
    ) {
        if (reviewStatus.isSameAsNotStarted()) {
            return runnerPostQueryRepository.joinSupporterRunnerPostBySupporterIdAndReviewStatus(pageable, supporterId, reviewStatus);
        }
        return runnerPostQueryRepository.findBySupporterIdAndReviewStatus(pageable, supporterId, reviewStatus);
    }

    public List<Long> readCountsByRunnerPostIds(final List<Long> runnerPostIds) {
        return supporterRunnerPostQueryRepository.countByRunnerPostIds(runnerPostIds);
    }

    public boolean existsRunnerPostApplicantByRunnerPostIdAndMemberId(final Long runnerPostId, final Long memberId) {
        return supporterRunnerPostQueryRepository.existsByRunnerPostIdAndMemberId(runnerPostId, memberId);
    }

    public long readCountByRunnerPostId(final Long runnerPostId) {
        return supporterRunnerPostQueryRepository.countByRunnerPostId(runnerPostId).orElse(0L);
    }

    @Transactional
    public void increaseWatchedCount(final RunnerPost runnerPost) {
        runnerPost.increaseWatchedCount();
    }
}
