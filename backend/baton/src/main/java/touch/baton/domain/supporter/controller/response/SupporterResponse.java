package touch.baton.domain.supporter.controller.response;

import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;

import java.util.List;

public record SupporterResponse() {

    public record Detail(Long supporterId,
                         String name,
                         String company,
                         int reviewCount,
                         String githubUrl,
                         String introduction,
                         List<String> supporterTechnicalTags
    ) {

        public static Detail from(final Supporter supporter) {
            return new Detail(
                    supporter.getId(),
                    supporter.getMember().getMemberName().getValue(),
                    supporter.getMember().getCompany().getValue(),
                    supporter.getReviewCount().getValue(),
                    supporter.getMember().getGithubUrl().getValue(),
                    supporter.getIntroduction().getValue(),
                    getSupporterTechnicalTagsName(supporter)
            );
        }

        private static List<String> getSupporterTechnicalTagsName(Supporter supporter) {
            return supporter.getSupporterTechnicalTags().getSupporterTechnicalTags()
                    .stream()
                    .map(SupporterTechnicalTag::getTechnicalTag)
                    .map(technicalTag -> technicalTag.getTagName().getValue())
                    .toList();
        }
    }
}
