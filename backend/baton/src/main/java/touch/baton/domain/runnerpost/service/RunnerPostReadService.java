package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runner.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.RunnerPostsApplicantCount;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.repository.dto.RunnerPostApplicantCountDto;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostReadService {

    private final RunnerPostRepository runnerPostRepository;

    public Page<RunnerPost> readRunnerPostByTagNameAndReviewStatus(final Pageable pageable,
                                                                   final String tagName,
                                                                   final ReviewStatus reviewStatus
    ) {
        final TagReducedName tagReducedName = TagReducedName.from(tagName);

        return runnerPostRepository.findByTagReducedNameAndReviewStatus(pageable, tagReducedName, reviewStatus);
    }

    public List<RunnerPostResponse.Simple> readRunnerPostByPageInfoAndReviewStatus(final Long cursor, final int limit, final ReviewStatus reviewStatus) {
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatus(cursor, limit, reviewStatus);
        final List<RunnerPostTag> runnerPostTags = runnerPostRepository.findByRunnerPosts(runnerPosts);
        final RunnerPostsApplicantCount runnerPostsApplicantCount = readRunnerPostsApplicantCount(runnerPosts);
        return convertToSimpleResponses(runnerPosts, runnerPostTags, runnerPostsApplicantCount);
    }

    public List<RunnerPostResponse.Simple> readLatestByLimitAndReviewStatus(final int limit, final ReviewStatus reviewStatus) {
        final List<RunnerPost> runnerPosts = runnerPostRepository.findLatestByLimitAndReviewStatus(limit, reviewStatus);
        final List<RunnerPostTag> runnerPostTags = runnerPostRepository.findByRunnerPosts(runnerPosts);
        final RunnerPostsApplicantCount runnerPostsApplicantCount = readRunnerPostsApplicantCount(runnerPosts);
        return convertToSimpleResponses(runnerPosts, runnerPostTags, runnerPostsApplicantCount);
    }

    private RunnerPostsApplicantCount readRunnerPostsApplicantCount(final List<RunnerPost> runnerPosts) {
        final List<Long> runnerPostIds = runnerPosts.stream()
                .map(RunnerPost::getId)
                .toList();
        final List<RunnerPostApplicantCountDto> runnerPostApplicantCountDtos = runnerPostRepository.countApplicantsByRunnerPostIds(runnerPostIds);
        return RunnerPostsApplicantCount.from(runnerPostApplicantCountDtos);
    }

    private List<RunnerPostResponse.Simple> convertToSimpleResponses(final List<RunnerPost> runnerPosts,
                                                                     final List<RunnerPostTag> runnerPostTags,
                                                                     final RunnerPostsApplicantCount runnerPostsApplicantCount
    ) {
        return runnerPosts.stream()
                .map(runnerPost -> convertToSimpleResponse(runnerPost, runnerPostsApplicantCount.getApplicantCountById(runnerPost.getId()), runnerPostTags))
                .toList();
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
                .filter(runnerPostTag -> runnerPostTag.getRunnerPost().getId() == runnerPost.getId())
                .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                .toList();
    }
}
