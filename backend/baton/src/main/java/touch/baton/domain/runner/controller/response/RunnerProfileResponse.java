package touch.baton.domain.runner.controller.response;

import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;

import java.util.List;
import java.util.Objects;

public record RunnerProfileResponse() {

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
}
