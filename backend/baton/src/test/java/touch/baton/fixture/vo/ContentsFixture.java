package touch.baton.fixture.vo;

import touch.baton.domain.common.vo.Contents;

public abstract class ContentsFixture {

    private ContentsFixture() {
    }

    public static Contents contents(final String value) {
        return new Contents(value);
    }
}
