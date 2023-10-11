package touch.baton.domain.runnerpost.query.controller.response;

import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.SupporterRunnerPost;

import java.util.List;

public record SupporterRunnerPostResponse() {

    public record Detail(Long supporterId,
                         String name,
                         String company,
                         int reviewCount,
                         String imageUrl,
                         String message,
                         List<String> technicalTags
    ) {

        public static Detail from(final SupporterRunnerPost supporterRunnerPost) {
            return new Detail(
                    supporterRunnerPost.getSupporter().getId(),
                    supporterRunnerPost.getSupporter().getMember().getMemberName().getValue(),
                    supporterRunnerPost.getSupporter().getMember().getCompany().getValue(),
                    supporterRunnerPost.getSupporter().getReviewCount().getValue(),
                    supporterRunnerPost.getSupporter().getMember().getImageUrl().getValue(),
                    supporterRunnerPost.getMessage().getValue(),
                    convertToTechnicalTags(supporterRunnerPost.getSupporter())
            );
        }

        private static List<String> convertToTechnicalTags(final Supporter supporter) {
            return supporter.getSupporterTechnicalTags().getSupporterTechnicalTags().stream()
                    .map(supporterTechnicalTag -> supporterTechnicalTag.getTechnicalTag().getTagName().getValue())
                    .toList();
        }
    }
}
