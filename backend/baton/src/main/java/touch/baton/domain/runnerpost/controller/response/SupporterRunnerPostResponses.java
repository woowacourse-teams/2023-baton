package touch.baton.domain.runnerpost.controller.response;

import java.util.List;

public record SupporterRunnerPostResponses() {

    public record All(List<SupporterRunnerPostResponse.All> data) {

        public static All from(final List<SupporterRunnerPostResponse.All> data) {
            return new SupporterRunnerPostResponses.All(data);
        }
    }
}
