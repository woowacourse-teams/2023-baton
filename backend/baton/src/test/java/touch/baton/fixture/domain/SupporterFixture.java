package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;

import static touch.baton.domain.common.vo.Grade.BARE_FOOT;
import static touch.baton.fixture.domain.MemberFixture.createDitoo;
import static touch.baton.fixture.domain.MemberFixture.createEthan;
import static touch.baton.fixture.domain.MemberFixture.createHyena;
import static touch.baton.fixture.domain.MemberFixture.createJudy;
import static touch.baton.fixture.vo.ReviewCountFixture.reviewCount;
import static touch.baton.fixture.vo.StarCountFixture.starCount;
import static touch.baton.fixture.vo.TotalRatingFixture.totalRating;

public abstract class SupporterFixture {

    private SupporterFixture() {
    }

    public static Supporter create(final ReviewCount reviewCount,
                                   final StarCount starCount,
                                   final TotalRating totalRating,
                                   final Grade grade,
                                   final Member member
    ) {
        return Supporter.builder()
                .reviewCount(reviewCount)
                .starCount(starCount)
                .totalRating(totalRating)
                .grade(grade)
                .member(member)
                .build();
    }

    public static Supporter createHyenaSupporter() {
        return create(
                reviewCount(100),
                starCount(0),
                totalRating(100),
                BARE_FOOT,
                createHyena());
    }

    public static Supporter createEthanSupporter() {
        return create(
                reviewCount(1),
                starCount(5),
                totalRating(5),
                BARE_FOOT,
                createEthan());
    }

    public static Supporter createDitooSupporter() {
        return create(
                reviewCount(10),
                starCount(30),
                totalRating(45),
                BARE_FOOT,
                createDitoo());
    }

    public static Supporter createJudySupporter() {
        return create(
                reviewCount(100),
                starCount(500),
                totalRating(500),
                BARE_FOOT,
                createJudy());
    }
}
