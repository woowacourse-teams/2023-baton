package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;

import static touch.baton.fixture.domain.MemberFixture.*;
import static touch.baton.fixture.vo.TotalRatingFixture.totalRating;

public abstract class RunnerFixture {

    private RunnerFixture() {
    }

    public static Runner create(final int totalRating, final Grade grade, final Member member) {
        return Runner.builder()
                .totalRating(totalRating(totalRating))
                .grade(grade)
                .member(member)
                .build();
    }

    public static Runner createHyenaRunner() {
        return create(100, Grade.BARE_FOOT, createHyena());
    }

    public static Runner createEthanRunner() {
        return create(50, Grade.BARE_FOOT, createEthan());
    }

    public static Runner createDitooRunner() {
        return create(1000, Grade.BARE_FOOT, createDitoo());
    }

    public static Runner createJudyRunner() {
        return create(5000, Grade.BARE_FOOT, createJudy());
    }
}
