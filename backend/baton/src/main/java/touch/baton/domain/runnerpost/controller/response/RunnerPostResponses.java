package touch.baton.domain.runnerpost.controller.response;

import java.util.List;

public record RunnerPostResponses() {

    public record Simple(List<RunnerPostResponse.Simple> data) {

        public static RunnerPostResponses.Simple from(final List<RunnerPostResponse.Simple> data) {
            return new RunnerPostResponses.Simple(data);
        }
    }
}
