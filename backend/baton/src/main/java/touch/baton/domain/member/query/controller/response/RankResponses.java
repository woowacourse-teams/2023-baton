package touch.baton.domain.member.query.controller.response;

import touch.baton.domain.member.command.Supporter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record RankResponses<T extends Rankable>(List<T> data) {

    public static RankResponses<SupporterRank> from(final List<Supporter> supporters) {
        final AtomicInteger rank = new AtomicInteger(1);
        final List<SupporterRank> responses = supporters.stream()
                .map(supporter -> new SupporterRank(
                        rank.getAndIncrement(),
                        supporter.getMember().getMemberName().getValue(),
                        supporter.getId(),
                        supporter.getReviewCount().getValue(),
                        supporter.getMember().getImageUrl().getValue(),
                        supporter.getMember().getGithubUrl().getValue(),
                        supporter.getMember().getCompany().getValue(),
                        supporter.getSupporterTechnicalTags().getSupporterTechnicalTags().stream()
                                .map(supporterTechnicalTag -> supporterTechnicalTag.getTechnicalTag().getTagName().getValue())
                                .toList()))
                .toList();

        return new RankResponses<>(responses);
    }

    public record SupporterRank(
            int rank,
            String name,
            long supporterId,
            int reviewedCount,
            String imageUrl,
            String githubUrl,
            String company,
            List<String> technicalTags
    ) implements Rankable {

    }
}
