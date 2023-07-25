package touch.baton.domain.supporter.controller.response;

import touch.baton.domain.supporter.Supporter;

public record SupporterResponse() {

    public record Detail(Long supporterId,
                         String name,
                         String company,
                         int reviewCount,
                         String githubUrl,
                         String introduction
    ) {

        public static Detail from(final Supporter supporter) {
            return new Detail(
                    supporter.getId(),
                    supporter.getMember().getMemberName().getValue(),
                    supporter.getMember().getCompany().getValue(),
                    supporter.getReviewCount().getValue(),
                    supporter.getMember().getGithubUrl().getValue(),
                    supporter.getIntroduction().getValue()
            );
        }
    }
}
