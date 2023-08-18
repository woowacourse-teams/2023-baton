package touch.baton.fixture.vo;

import touch.baton.domain.common.vo.Introduction;

public abstract class IntroductionFixture {

    private IntroductionFixture() {
    }

    public static Introduction introduction(final String value) {
        return new Introduction(value);
    }
}
