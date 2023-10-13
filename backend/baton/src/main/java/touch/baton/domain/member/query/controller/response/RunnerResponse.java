package touch.baton.domain.member.query.controller.response;

import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;

import java.util.List;

public record RunnerResponse() {

    public record Detail(Long runnerId,
                         String name,
                         String imageUrl,
                         String githubUrl,
                         String introduction,
                         String company,
                         List<String> technicalTags
    ) {

        public static Detail from(final Runner runner) {
            final List<String> tagNames = runner.getRunnerTechnicalTags().getRunnerTechnicalTags().stream()
                    .map(runnerTechnicalTag -> runnerTechnicalTag.getTechnicalTag().getTagName().getValue())
                    .toList();

            final Member member = runner.getMember();
            return new Detail(runner.getId(),
                    member.getMemberName().getValue(),
                    member.getImageUrl().getValue(),
                    member.getGithubUrl().getValue(),
                    runner.getIntroduction().getValue(),
                    member.getCompany().getValue(),
                    tagNames);
        }
    }

    public record InRunnerPostDetail(Long runnerId,
                                     String name,
                                     String company,
                                     String imageUrl
    ) {

        public static InRunnerPostDetail from(final Runner runner) {
            return new InRunnerPostDetail(
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
                       String company,
                       String imageUrl,
                       String githubUrl,
                       String introduction,
                       List<String> technicalTags
    ) {

        public static Mine from(final Runner runner) {
            return new Mine(
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
