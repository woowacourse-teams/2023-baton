package touch.baton.domain.common.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RunnerPostReadResponse {

    private List<RunnerPostResponse> runnerPosts;

    public RunnerPostReadResponse(List<RunnerPostResponse> runnerPosts) {
        this.runnerPosts = runnerPosts;
    }

    public static RunnerPostReadResponse fromRunnerPostResponses(List<RunnerPostResponse> runnerPostsResponses){
        return new RunnerPostReadResponse(runnerPostsResponses);
    }
}
