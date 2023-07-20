package touch.baton.domain.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import touch.baton.domain.runnerpost.RunnerPost;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RunnerPostResponse {

    private Long runnerPostId;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime deadline;

    private List<String> tags;

    private String name;

    private int watchedCount;

    private int chattingCount;

    private String imageUrl;

    public RunnerPostResponse(Long runnerPostId,
                              String title,
                              LocalDateTime deadline,
                              List<String> tags,
                              String name,
                              int watchedCount,
                              int chattingCount,
                              String imageUrl) {
        this.runnerPostId = runnerPostId;
        this.title = title;
        this.deadline = deadline;
        this.tags = tags;
        this.name = name;
        this.watchedCount = watchedCount;
        this.chattingCount = chattingCount;
        this.imageUrl = imageUrl;
    }

    public static RunnerPostResponse fromRunnerPost(RunnerPost runnerPost) {
        return new RunnerPostResponse(
                runnerPost.getId(),
                runnerPost.getTitle().getValue(),
                runnerPost.getDeadline().getValue(),
                runnerPost.getRunnerPostTags()
                        .getRunnerPostTags()
                        .stream()
                        .map(runnerPostTag -> runnerPostTag.getTag().toString())
                        .toList(),
                runnerPost.getRunner().getMember().getMemberName().getValue(),
                runnerPost.getWatchedCount().getValue(),
                runnerPost.getChattingCount().getValue(),
                runnerPost.getRunner().getMember().getImageUrl().getValue()
        );
    }
    //runnerPost들을 만들어줘야함

    public static List<RunnerPostResponse> fromRunnerPosts(List<RunnerPost> runnerPosts) {
        return runnerPosts.stream()
                .map(runnerPostResponse -> fromRunnerPost(runnerPostResponse))
                .toList();
    }
}
