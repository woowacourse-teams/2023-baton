package touch.baton.domain.common.response;

import lombok.Getter;
import touch.baton.domain.runnerpost.RunnerPost;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RunnerPostResponse {

    private Long runnerPostId;

    private String title;

    private String deadline;

    private String[] tags;

    //private Object[] profile;
    private String name;
    //private String imageUrl;

    public RunnerPostResponse() {
    }

    public RunnerPostResponse(Long runnerPostId, String title, String deadline, String[] tags, String name) {
        this.runnerPostId = runnerPostId;
        this.title = title;
        this.deadline = deadline;
        this.tags = tags;
        this.name = name;
    }

    public static RunnerPostResponse fromRunnerPost(RunnerPost runnerPost) {
        return new RunnerPostResponse(
                runnerPost.getId(),
                runnerPost.getTitle().getValue(),
                runnerPost.getDeadline().getValue().toString(),
                (String[]) runnerPost.getRunnerPostTags().getRunnerPostTags().toArray(),
                runnerPost.getRunner().getMember().getMemberName().getValue()
        );
    }

    public static List<RunnerPostResponse> convert(List<RunnerPost> runnerPosts) {
        List<RunnerPostResponse> response = runnerPosts.stream()
                .map(RunnerPostResponse::fromRunnerPost)
                .collect(Collectors.toList());
        return response;
    }
}
