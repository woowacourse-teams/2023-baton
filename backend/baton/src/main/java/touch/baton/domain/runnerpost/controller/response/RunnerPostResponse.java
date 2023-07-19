package touch.baton.domain.runnerpost.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagName;

import java.time.LocalDateTime;
import java.util.List;

public record RunnerPostResponse() {

    public record SingleRunnerPost(Long runnerPostId,
                                   String title,
                                   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
                                   LocalDateTime deadLine,
                                   List<String> tags,
                                   String contents,
                                   Integer chattingCount,
                                   Integer watchedCount,
                                   ProfileResponse profile
    ) {
        public static SingleRunnerPost from(final RunnerPost runnerPost) {
            return new SingleRunnerPost(
                    runnerPost.getId(),
                    runnerPost.getTitle().getValue(),
                    runnerPost.getDeadline().getValue(),
                    collectTagNameValues(runnerPost),
                    runnerPost.getContents().getValue(),
                    runnerPost.getChattingRoomCount().getValue(),
                    runnerPost.getWatchedCount().getValue(),
                    ProfileResponse.from(runnerPost.getRunner().getMember())
            );
        }

        private static List<String> collectTagNameValues(final RunnerPost runnerPost) {
            return runnerPost.getRunnerPostTags()
                    .getRunnerPostTags()
                    .stream()
                    .map(RunnerPostTag::getTag)
                    .map(Tag::getTagName)
                    .map(TagName::getValue)
                    .toList();
        }
    }
}
