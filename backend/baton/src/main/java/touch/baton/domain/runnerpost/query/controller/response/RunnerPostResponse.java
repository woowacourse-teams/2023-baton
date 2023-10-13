package touch.baton.domain.runnerpost.query.controller.response;

import touch.baton.domain.common.response.IdExtractable;
import touch.baton.domain.member.query.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.tag.command.RunnerPostTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record RunnerPostResponse() {

    public record Detail(Long runnerPostId,
                         String title,
                         String implementedContents,
                         String curiousContents,
                         String postscriptContents,
                         String pullRequestUrl,
                         LocalDateTime deadline,
                         int watchedCount,
                         long applicantCount,
                         ReviewStatus reviewStatus,
                         boolean isOwner,
                         boolean isApplied,
                         List<String> tags,
                         RunnerResponse.InRunnerPostDetail runnerProfile
    ) {

        public static Detail of(final RunnerPost runnerPost,
                                final boolean isOwner,
                                final boolean isApplied,
                                final long applicantCount
        ) {
            return new Detail(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getImplementedContents().getValue(),
                    runnerPost.getCuriousContents().getValue(),
                    runnerPost.getPostscriptContents().getValue(),
                    runnerPost.getPullRequestUrl().getValue(),
                    runnerPost.getDeadline().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    applicantCount,
                    runnerPost.getReviewStatus(),
                    isOwner,
                    isApplied,
                    convertToTags(runnerPost),
                    RunnerResponse.InRunnerPostDetail.from(runnerPost.getRunner())
            );
        }
    }

    public record Simple(Long runnerPostId,
                         String title,
                         LocalDateTime deadline,
                         int watchedCount,
                         long applicantCount,
                         String reviewStatus,
                         RunnerResponse.Simple runnerProfile,
                         List<String> tags
    ) implements IdExtractable {

        public static Simple of(final RunnerPost runnerPost,
                                final long applicantCount,
                                final List<RunnerPostTag> runnerPostTags
        ) {
            return new Simple(
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

        @Override
        public Long extractId() {
            return runnerPostId;
        }
    }

    public record SimpleByRunner(Long runnerPostId,
                                 String title,
                                 LocalDateTime deadline,
                                 int watchedCount,
                                 long applicantCount,
                                 String reviewStatus,
                                 boolean isReviewed,
                                 Long supporterId,
                                 List<String> tags

    ) implements IdExtractable {

        public static SimpleByRunner of(final RunnerPost runnerPost,
                                        final long applicantCount,
                                        final List<RunnerPostTag> runnerPostTags
        ) {
            return new SimpleByRunner(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    applicantCount,
                    runnerPost.getReviewStatus().name(),
                    runnerPost.getIsReviewed().getValue(),
                    getSupporterIdByRunnerPost(runnerPost),
                    convertToTags(runnerPost, runnerPostTags)
            );
        }

        private static Long getSupporterIdByRunnerPost(final RunnerPost runnerPost) {
            if (Objects.isNull(runnerPost.getSupporter())) {
                return null;
            }
            return runnerPost.getSupporter().getId();
        }

        @Override
        public Long extractId() {
            return runnerPostId;
        }
    }

    public record Count(Long count) {

        public static Count from(final long count) {
            return new Count(count);
        }
    }

    private static List<String> convertToTags(final RunnerPost runnerPost) {
        return runnerPost.getRunnerPostTags()
                .getRunnerPostTags()
                .stream()
                .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                .toList();
    }

    private static List<String> convertToTags(final RunnerPost runnerPost, final List<RunnerPostTag> runnerPostTags) {
        return runnerPostTags.stream()
                .filter(runnerPostTag -> Objects.equals(runnerPostTag.getRunnerPost().getId(), runnerPost.getId()))
                .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                .toList();
    }
}
