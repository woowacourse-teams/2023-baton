package touch.baton.fixture.vo;

import touch.baton.domain.runnerpost.vo.CuriousContents;

public abstract class CuriousContentsFixture {

    private CuriousContentsFixture() {
    }

    public static CuriousContents curiousContents(final String value) {
        return new CuriousContents(value);
    }
}
