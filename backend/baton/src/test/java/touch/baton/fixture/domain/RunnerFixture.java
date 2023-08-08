package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;

import static touch.baton.fixture.vo.IntroductionFixture.introduction;

public abstract class RunnerFixture {

    private RunnerFixture() {
    }

    public static Runner create(final Introduction introduction,
                                final Member member
    ) {
        return Runner.builder()
                .introduction(introduction)
                .member(member)
                .build();
    }

    public static Runner createRunner(final Member member) {
        return create(introduction("안녕하세요."), member);
    }
}
