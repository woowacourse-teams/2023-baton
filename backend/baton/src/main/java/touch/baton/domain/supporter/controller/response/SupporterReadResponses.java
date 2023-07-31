package touch.baton.domain.supporter.controller.response;

import java.util.List;

public record SupporterReadResponses() {

    public record NoFiltering(List<SupporterResponse.Detail> data) {

        public static NoFiltering from(final List<SupporterResponse.Detail> data) {
            return new SupporterReadResponses.NoFiltering(data);
        }
    }
}
