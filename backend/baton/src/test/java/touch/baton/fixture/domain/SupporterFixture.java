package touch.baton.fixture.domain;

import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.ReviewCount;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTags;
import touch.baton.domain.technicaltag.command.TechnicalTag;
import touch.baton.fixture.vo.ReviewCountFixture;

import java.util.ArrayList;
import java.util.List;

public abstract class SupporterFixture {

    private SupporterFixture() {
    }

    public static Supporter create(final Member member) {
        return Supporter.builder()
                .reviewCount(ReviewCountFixture.reviewCount(0))
                .member(member)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();
    }

    public static Supporter create(final ReviewCount reviewCount,
                                   final Member member
    ) {
        return Supporter.builder()
                .reviewCount(reviewCount)
                .member(member)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();
    }

    public static Supporter create(final ReviewCount reviewCount,
                                   final Member member,
                                   final SupporterTechnicalTags supporterTechnicalTags
    ) {
        return Supporter.builder()
                .reviewCount(reviewCount)
                .member(member)
                .supporterTechnicalTags(supporterTechnicalTags)
                .build();
    }

    public static Supporter create(final Member member,
                                   final List<TechnicalTag> technicalTags
    ) {
        return create(new ReviewCount(0), member, technicalTags);
    }

    public static Supporter create(final ReviewCount reviewCount,
                                   final Member member,
                                   final List<TechnicalTag> technicalTags
    ) {
        final Supporter supporter = Supporter.builder()
                .reviewCount(reviewCount)
                .member(member)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();

        final List<SupporterTechnicalTag> supporterTechnicalTags = technicalTags.stream()
                .map(technicalTag -> SupporterTechnicalTag.builder()
                        .supporter(supporter)
                        .technicalTag(technicalTag)
                        .build())
                .toList();

        supporter.addAllSupporterTechnicalTags(supporterTechnicalTags);
        return supporter;
    }
}
