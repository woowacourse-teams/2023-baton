package touch.baton.fixture.vo;

import touch.baton.domain.runnerpost.command.vo.ImplementedContents;

public abstract class ImplementedContentsFixture {

    private ImplementedContentsFixture() {
    }

    public static ImplementedContents implementedContents(final String value) {
        return new ImplementedContents(value);
    }
}
