package touch.baton.domain.runnerpost.controller.response;

import java.util.List;

public record SupporterRunnerPostResponses() {

    public record All(List<SupporterRunnerPostResponse.Detail> data) {

        public static All from(final List<SupporterRunnerPostResponse.Detail> data) {
            return new SupporterRunnerPostResponses.All(data);
        }
    }
}
