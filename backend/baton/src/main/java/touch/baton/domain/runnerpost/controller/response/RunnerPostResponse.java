package touch.baton.domain.runnerpost.controller.response;

import touch.baton.domain.runner.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;

public record RunnerPostResponse() {

    public record Detail(Long runnerPostId,
                         String title,
                         String contents,
                         String pullRequestUrl,
                         LocalDateTime deadline,
                         Integer watchedCount,
                         ReviewStatus reviewStatus,
                         boolean isOwner,
                         RunnerResponse.Detail runnerProfile,
                         List<String> tags
    ) {

        public static Detail of(final RunnerPost runnerPost, final boolean isOwner) {
            return new Detail(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getContents().getValue(),
                    runnerPost.getPullRequestUrl().getValue(),
                    runnerPost.getDeadline().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    runnerPost.getReviewStatus(),
                    isOwner,
                    RunnerResponse.Detail.from(runnerPost.getRunner()),
                    convertToTags(runnerPost)
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
                         String reviewStatus,
                         RunnerResponse.Simple runnerProfile,
                         List<String> tags
    ) {

        public static Simple from(final RunnerPost runnerPost) {
            return new Simple(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    runnerPost.getReviewStatus().name(),
                    RunnerResponse.Simple.from(runnerPost.getRunner()),
                    convertToTags(runnerPost)
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

    private static List<String> convertToTags(final RunnerPost runnerPost) {
        return runnerPost.getRunnerPostTags()
                .getRunnerPostTags()
                .stream()
                .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                .toList();
    }
}
