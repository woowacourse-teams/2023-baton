package touch.baton.domain.runner.controller.response;

import touch.baton.domain.runner.Runner;

import java.util.List;

public record RunnerResponse() {

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

    public record Mine(String name,
                       String imageUrl,
                       String githubUrl,
                       String introduction
    ) {

        public static Mine from(final Runner runner) {
            return new Mine(
                    runner.getMember().getMemberName().getValue(),
                    runner.getMember().getImageUrl().getValue(),
                    runner.getMember().getGithubUrl().getValue(),
                    runner.getIntroduction().getValue()
            );
        }
    }

    public record MyProfile(String name,
                            String company,
                            String imageUrl,
                            String githubUrl,
                            String introduction,
                            List<String> technicalTags
    ) {

        public static MyProfile from(final Runner runner) {
            return new MyProfile(
                    runner.getMember().getMemberName().getValue(),
                    runner.getMember().getCompany().getValue(),
                    runner.getMember().getImageUrl().getValue(),
                    runner.getMember().getGithubUrl().getValue(),
                    runner.getIntroduction().getValue(),
                    convertRunnerTechnicalTags(runner)
            );
        }

        private static List<String> convertRunnerTechnicalTags(final Runner runner) {
            return runner.getRunnerTechnicalTags().getRunnerTechnicalTags().stream()
                    .map(runnerTechnicalTag -> runnerTechnicalTag.getTechnicalTag().getTagName().getValue())
                    .toList();
        }
    }
}
