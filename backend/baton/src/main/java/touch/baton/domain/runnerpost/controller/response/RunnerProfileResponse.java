package touch.baton.domain.runnerpost.controller.response;

import touch.baton.domain.runner.Runner;

public record RunnerProfileResponse() {

    public record Detail(Long runnerId,
                         String name,
                         String company,
                         String imageUrl
    ) {

        public static Detail from(final Runner runner) {
            return new Detail(
                    runner.getId(),
                    runner.getMember().getMemberName().getValue(),
                    runner.getMember().getCompany().getValue(),
                    runner.getMember().getImageUrl().getValue()
            );
        }
    }

    public record Simple(String name, String imageUrl) {

        public static Simple from(final Runner runner) {
            return new Simple(
                    runner.getMember().getMemberName().getValue(),
                    runner.getMember().getImageUrl().getValue()
            );
        }
    }
}
