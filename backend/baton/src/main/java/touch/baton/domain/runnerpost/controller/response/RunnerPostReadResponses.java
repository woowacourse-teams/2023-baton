package touch.baton.domain.runnerpost.controller.response;

import java.util.List;

public record RunnerPostReadResponses() {

    public record NoFiltering(List<RunnerPostResponse.Simple> data) {

        public static NoFiltering from(final List<RunnerPostResponse.Simple> data) {
            return new NoFiltering(data);
        }
    }

    public record SimpleInMyPage(List<RunnerPostResponse.SimpleInMyPage> data) {

        public static SimpleInMyPage from(final List<RunnerPostResponse.SimpleInMyPage> data) {
            return new SimpleInMyPage(data);
        }
    }

    public record LoginedSupporter(List<RunnerPostResponse.LoginedSupporter> data) {

        public static LoginedSupporter from(final List<RunnerPostResponse.LoginedSupporter> data) {
            return new LoginedSupporter(data);
        }
    }
}
