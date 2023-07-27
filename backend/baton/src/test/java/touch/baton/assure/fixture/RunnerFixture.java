package touch.baton.assure.fixture;

import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;

public abstract class RunnerFixture {

    private RunnerFixture() {
    }

    public static Runner from(final Member member,
                              final TotalRating totalRating,
                              final Grade grade
    ) {
        return Runner.builder()
                .totalRating(totalRating)
                .grade(grade)
                .member(member)
                .build();
    }
}
