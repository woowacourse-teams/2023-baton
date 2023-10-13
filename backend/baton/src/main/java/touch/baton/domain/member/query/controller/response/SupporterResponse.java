package touch.baton.domain.member.query.controller.response;

import touch.baton.domain.member.command.Supporter;

import java.util.List;

public record SupporterResponse() {

    public record Detail(Long supporterId,
                         String name,
                         String company,
                         int reviewCount,
                         String githubUrl,
                         String introduction,
                         List<String> technicalTags
    ) {

        public static Detail from(final Supporter supporter) {
            return new Detail(
                    supporter.getId(),
                    supporter.getMember().getMemberName().getValue(),
                    supporter.getMember().getCompany().getValue(),
                    supporter.getReviewCount().getValue(),
                    supporter.getMember().getGithubUrl().getValue(),
                    supporter.getIntroduction().getValue(),
                    convertToTechnicalTags(supporter)
            );
        }
    }

    public record Profile(Long supporterId,
                          String name,
                          String company,
                          String imageUrl,
                          String githubUrl,
                          String introduction,
                          List<String> technicalTags
    ) {
        public static SupporterResponse.Profile from(final Supporter supporter) {
            return new SupporterResponse.Profile(
                    supporter.getId(),
                    supporter.getMember().getMemberName().getValue(),
                    supporter.getMember().getCompany().getValue(),
                    supporter.getMember().getImageUrl().getValue(),
                    supporter.getMember().getGithubUrl().getValue(),
                    supporter.getIntroduction().getValue(),
                    convertToTechnicalTags(supporter)
            );
        }
    }

    public record MyProfile(
            String name,
            String imageUrl,
            String githubUrl,
            String introduction,
            String company,
            List<String> technicalTags
    ) {

        public static MyProfile from(final Supporter supporter) {
            return new SupporterResponse.MyProfile(
                    supporter.getMember().getMemberName().getValue(),
                    supporter.getMember().getImageUrl().getValue(),
                    supporter.getMember().getGithubUrl().getValue(),
                    supporter.getIntroduction().getValue(),
                    supporter.getMember().getCompany().getValue(),
                    convertToTechnicalTags(supporter)
            );
        }
    }

    private static List<String> convertToTechnicalTags(final Supporter supporter) {
        return supporter.getSupporterTechnicalTags().getSupporterTechnicalTags()
                .stream()
                .map(supporterTechnicalTag -> supporterTechnicalTag.getTechnicalTag().getTagName().getValue())
                .toList();
    }
}
