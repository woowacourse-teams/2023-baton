package touch.baton.domain.runnerpost.controller.response;

import touch.baton.domain.supporter.Supporter;

public record SupporterResponseTestVersion() {

    public record Simple(Long supporterId, String name) {

        public static Simple fromTestVersion(final Supporter supporter) {
            return new Simple(
                    supporter.getId(),
                    supporter.getMember().getMemberName().getValue()
            );
        }
    }

}
