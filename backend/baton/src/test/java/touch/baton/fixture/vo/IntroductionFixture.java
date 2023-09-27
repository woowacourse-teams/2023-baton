package touch.baton.fixture.vo;

import touch.baton.domain.member.command.vo.Introduction;

public abstract class IntroductionFixture {

    private IntroductionFixture() {
    }

    public static Introduction introduction(final String value) {
        return new Introduction(value);
    }
}
