package touch.baton.domain.runnerpost.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import touch.baton.domain.runnerpost.RunnerPost;

import java.time.LocalDateTime;
import java.util.List;

public record RunnerPostResponse() {

    public record Detail(Long runnerPostId,
                         String title,
                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
                         LocalDateTime deadline,
                         List<String> tags,
                         String contents,
                         Integer chattingCount,
                         Integer watchedCount,
                         boolean isOwner,
                         ProfileResponse.Detail profile
    ) {

        public static Detail from(final RunnerPost runnerPost, final Long runnerId) {
            return new Detail(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    convertToTags(runnerPost),
                    runnerPost.getContents().getValue(),
                    runnerPost.getChattingCount().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    checkIsOwner(runnerPost.getRunner().getId(), runnerId),
                    ProfileResponse.Detail.from(runnerPost.getRunner().getMember())
            );
        }

        private static boolean checkIsOwner(final Long runnerPostRunnerId, final Long runnerId) {
            if(runnerPostRunnerId == runnerId){
                return true;
            }
            return false;
        }
    }

    public record Simple(Long runnerPostId,
                         String title,
                         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
                         LocalDateTime deadline,
                         List<String> tags,
                         ProfileResponse.Simple profile,
                         int watchedCount,
                         int chattingCount,
                         String reviewStatus
    ) {

        public static Simple from(final RunnerPost runnerPost) {
            return new Simple(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    convertToTags(runnerPost),
                    ProfileResponse.Simple.from(runnerPost.getRunner().getMember()),
                    runnerPost.getWatchedCount().getValue(),
                    runnerPost.getChattingCount().getValue(),
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
