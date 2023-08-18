package touch.baton.fixture.vo;

import touch.baton.domain.common.vo.Title;

public abstract class TitleFixture {

    private TitleFixture() {
    }

    public static Title title(final String value) {
        return new Title(value);
    }
}
