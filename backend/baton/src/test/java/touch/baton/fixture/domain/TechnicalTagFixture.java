package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.TagName;
import touch.baton.tobe.domain.technicaltag.command.TechnicalTag;

import static touch.baton.fixture.vo.TagNameFixture.tagName;

public abstract class TechnicalTagFixture {

    private TechnicalTagFixture() {
    }

    public static TechnicalTag create(final TagName tagName) {
        return TechnicalTag.builder()
                .tagName(tagName)
                .build();
    }

    public static TechnicalTag createJava() {
        return create(tagName("Java"));
    }

    public static TechnicalTag createSpring() {
        return create(tagName("Spring"));
    }

    public static TechnicalTag createReact() {
        return create(tagName("React"));
    }
}
