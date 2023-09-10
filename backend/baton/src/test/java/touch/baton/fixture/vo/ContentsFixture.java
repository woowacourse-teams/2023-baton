package touch.baton.fixture.vo;

import touch.baton.domain.runnerpost.vo.ImplementedContents;

public abstract class ContentsFixture {

    private ContentsFixture() {
    }

    public static ImplementedContents contents(final String value) {
        return new ImplementedContents(value);
    }
}
