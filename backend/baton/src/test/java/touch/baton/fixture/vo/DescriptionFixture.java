package touch.baton.fixture.vo;

import touch.baton.domain.feedback.vo.Description;

public class DescriptionFixture {

    private DescriptionFixture() {
    }

    public static Description description(final String value) {
        return new Description(value);
    }
}
