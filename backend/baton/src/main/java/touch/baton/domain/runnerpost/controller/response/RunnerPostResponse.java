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
                         Integer chattingCount,
                         ReviewStatus reviewStatus,
                         boolean isOwner,
                         RunnerResponse.Detail runnerProfile,
                         List<String> tags
    ) {

        public static Detail from(final RunnerPost runnerPost) {
            return new Detail(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getContents().getValue(),
                    runnerPost.getPullRequestUrl().getValue(),
                    runnerPost.getDeadline().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    runnerPost.getChattingCount().getValue(),
                    runnerPost.getReviewStatus(),
                    true,
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
                                    Integer chattingCount,
                                    ReviewStatus reviewStatus,
                                    RunnerResponse.Detail runnerProfile,
                                    SupporterResponseTestVersion.Simple supporterProfile,
                                    boolean isOwner,
                                    List<String> tags
    ) {
        public static DetailVersionTest fromVersionTest(final RunnerPost runnerPost) {
            return new DetailVersionTest(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getContents().getValue(),
                    runnerPost.getPullRequestUrl().getValue(),
                    runnerPost.getDeadline().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    runnerPost.getChattingCount().getValue(),
                    runnerPost.getReviewStatus(),
                    RunnerResponse.Detail.from(runnerPost.getRunner()),
                    SupporterResponseTestVersion.Simple.fromTestVersion(runnerPost.getSupporter()),
                    true,
                    convertToTags(runnerPost)
            );
        }
    }

    public record Simple(Long runnerPostId,
                         String title,
                         LocalDateTime deadline,
                         int watchedCount,
                         int chattingCount,
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
                    runnerPost.getChattingCount().getValue(),
                    runnerPost.getReviewStatus().name(),
                    RunnerResponse.Simple.from(runnerPost.getRunner()),
                    convertToTags(runnerPost)
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
