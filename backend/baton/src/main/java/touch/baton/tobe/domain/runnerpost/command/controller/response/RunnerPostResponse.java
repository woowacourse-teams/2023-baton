package touch.baton.tobe.domain.runnerpost.command.controller.response;

import touch.baton.tobe.domain.member.query.controller.response.RunnerResponse;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.runnerpost.command.vo.ReviewStatus;

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
                         RunnerResponse.Detail runnerProfile
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
                    RunnerResponse.Detail.from(runnerPost.getRunner())
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
    ) {

        public static Simple from(final RunnerPost runnerPost,
                                  final long applicantCount
        ) {
            return new Simple(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    applicantCount,
                    runnerPost.getReviewStatus().name(),
                    RunnerResponse.Simple.from(runnerPost.getRunner()),
                    convertToTags(runnerPost)
            );
        }
    }

    public record LoginedSupporter(Long runnerPostId,
                                   String title,
                                   LocalDateTime deadline,
                                   List<String> tags,
                                   int watchedCount,
                                   int applicantCount

    ) {

        public static LoginedSupporter from(final RunnerPost runnerPost, final int applicantCount) {
            return new LoginedSupporter(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    convertToTags(runnerPost),
                    runnerPost.getWatchedCount().getValue(),
                    applicantCount
            );
        }
    }

    public record Mine(Long runnerPostId,
                       String title,
                       LocalDateTime deadline,
                       List<String> tags,
                       String reviewStatus
    ) {

        public static Mine from(final RunnerPost runnerPost) {
            return new Mine(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    convertToTags(runnerPost),
                    runnerPost.getReviewStatus().name()
            );
        }
    }

    public record SimpleInMyPage(Long runnerPostId,
                                 Long supporterId,
                                 String title,
                                 LocalDateTime deadline,
                                 List<String> tags,
                                 int watchedCount,
                                 long applicantCount,
                                 String reviewStatus,
                                 boolean isReviewed

    ) {

        public static SimpleInMyPage from(final RunnerPost runnerPost,
                                          final long applicantCount
        ) {
            return new SimpleInMyPage(
                    runnerPost.getId(),
                    getSupporterIdByRunnerPost(runnerPost),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    convertToTags(runnerPost),
                    runnerPost.getWatchedCount().getValue(),
                    applicantCount,
                    runnerPost.getReviewStatus().name(),
                    runnerPost.getIsReviewed().getValue()
            );
        }

        private static Long getSupporterIdByRunnerPost(final RunnerPost runnerPost) {
            if (Objects.isNull(runnerPost.getSupporter())) {
                return null;
            }
            return runnerPost.getSupporter().getId();
        }
    }

    public record ReferencedBySupporter(Long runnerPostId,
                                        String title,
                                        LocalDateTime deadline,
                                        List<String> tags,
                                        int watchedCount,
                                        long applicantCount,
                                        String reviewStatus
    ) {

        public static ReferencedBySupporter of(final RunnerPost runnerPost, final long applicantCount) {
            return new ReferencedBySupporter(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    convertToTags(runnerPost),
                    runnerPost.getWatchedCount().getValue(),
                    applicantCount,
                    runnerPost.getReviewStatus().name()
            );
        }
    }

    private static List<String> convertToTags(final RunnerPost runnerPost) {
        return runnerPost.getRunnerPostTags()
                .getRunnerPostTags()
                .stream()
                .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                .toList();
    }
}
