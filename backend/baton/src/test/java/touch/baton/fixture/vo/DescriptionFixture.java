package touch.baton.fixture.vo;

import touch.baton.domain.feedback.command.vo.Description;

public abstract class DescriptionFixture {

    private DescriptionFixture() {
    }

    public static Description description(final String value) {
        return new Description(value);
    }
}
