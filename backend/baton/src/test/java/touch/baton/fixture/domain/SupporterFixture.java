package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;

import static touch.baton.domain.common.vo.Grade.*;
import static touch.baton.fixture.domain.MemberFixture.*;
import static touch.baton.fixture.vo.ReviewCountFixture.reviewCount;
import static touch.baton.fixture.vo.StarCountFixture.*;
import static touch.baton.fixture.vo.TotalRatingFixture.*;

public abstract class SupporterFixture {

    private SupporterFixture() {
    }

    public static Supporter create(final int reviewCount,
                                   final int starCount,
                                   final int totalRating,
                                   final Grade grade,
                                   final Member member
    ) {
        return Supporter.builder()
                .reviewCount(reviewCount(reviewCount))
                .starCount(starCount(starCount))
                .totalRating(totalRating(totalRating))
                .grade(grade)
                .member(member)
                .build();
    }

    public static Supporter createHyenaSupporter() {
        return create(100, 0, 100, BARE_FOOT, createHyena());
    }

    public static Supporter createEthanSupporter() {
        return create(1, 5, 5, BARE_FOOT, createEthan());
    }

    public static Supporter createDitooSupporter() {
        return create(10, 30, 45, BARE_FOOT, createDitoo());
    }

    public static Supporter createJudySupporter() {
        return create(100, 500, 500, BARE_FOOT, createJudy());
    }
}
