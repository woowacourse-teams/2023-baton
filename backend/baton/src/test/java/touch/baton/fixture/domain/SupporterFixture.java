package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;
import touch.baton.domain.technicaltag.TechnicalTag;

import java.util.ArrayList;
import java.util.List;

public abstract class SupporterFixture {

    private SupporterFixture() {
    }

    public static Supporter create(final ReviewCount reviewCount,
                                   final StarCount starCount,
                                   final TotalRating totalRating,
                                   final Grade grade,
                                   final Member member,
                                   final SupporterTechnicalTags supporterTechnicalTags
                                   ) {
        return Supporter.builder()
                .reviewCount(reviewCount)
                .starCount(starCount)
                .totalRating(totalRating)
                .grade(grade)
                .member(member)
                .supporterTechnicalTags(supporterTechnicalTags)
                .build();
    }

    public static Supporter create(final ReviewCount reviewCount,
                                   final StarCount starCount,
                                   final TotalRating totalRating,
                                   final Grade grade,
                                   final Member member,
                                   final List<TechnicalTag> technicalTags
    ) {
        final Supporter supporter =  Supporter.builder()
                .reviewCount(reviewCount)
                .starCount(starCount)
                .totalRating(totalRating)
                .grade(grade)
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
