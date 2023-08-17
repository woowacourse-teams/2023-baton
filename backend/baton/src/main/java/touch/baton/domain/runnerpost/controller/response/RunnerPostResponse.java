package touch.baton.domain.runnerpost.controller.response;

import touch.baton.domain.runner.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record RunnerPostResponse() {

    public record Detail(Long runnerPostId,
                         String title,
                         String contents,
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
                    runnerPost.getContents().getValue(),
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

    public record DetailVersionTest(Long runnerPostId,
                                    String title,
                                    String contents,
                                    String pullRequestUrl,
                                    LocalDateTime deadline,
                                    Integer watchedCount,
                                    ReviewStatus reviewStatus,
                                    RunnerResponse.Detail runnerProfile,
                                    SupporterResponseTestVersion.Simple supporterProfile,
                                    boolean isOwner,
                                    List<String> tags
    ) {

        public static DetailVersionTest ofVersionTest(final RunnerPost runnerPost, final boolean isOwner) {
            return new DetailVersionTest(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getContents().getValue(),
                    runnerPost.getPullRequestUrl().getValue(),
                    runnerPost.getDeadline().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    runnerPost.getReviewStatus(),
                    RunnerResponse.Detail.from(runnerPost.getRunner()),
                    SupporterResponseTestVersion.Simple.fromTestVersion(runnerPost.getSupporter()),
                    isOwner,
                    convertToTags(runnerPost)
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
                                 String reviewStatus

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
                    runnerPost.getReviewStatus().name()
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
