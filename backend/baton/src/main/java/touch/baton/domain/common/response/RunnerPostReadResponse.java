package touch.baton.domain.common.response;

import lombok.Getter;

import java.util.List;

@Getter
public class RunnerPostReadResponse {

    private List<RunnerPostResponse> data;

    public RunnerPostReadResponse(List<RunnerPostResponse> data) {
        this.data = data;
    }

    public static RunnerPostReadResponse fromRunnerPostResponses(List<RunnerPostResponse> data){
        return new RunnerPostReadResponse(data);
    }
}
