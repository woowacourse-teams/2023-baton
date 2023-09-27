package touch.baton.domain.runnerpost.command.controller.response;

import java.util.List;

public record SupporterRunnerPostResponses() {

    public record Detail(List<SupporterRunnerPostResponse.Detail> data) {

        public static Detail from(final List<SupporterRunnerPostResponse.Detail> data) {
            return new Detail(data);
        }
    }
}
