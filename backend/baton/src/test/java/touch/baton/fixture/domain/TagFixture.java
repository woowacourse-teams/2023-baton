package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagReducedName;
import touch.baton.fixture.vo.TagNameFixture;

public abstract class TagFixture {

    private TagFixture() {
    }

    public static Tag create(final TagName tagName) {
        return Tag.builder()
                .tagName(tagName)
                .tagReducedName(TagReducedName.from(tagName.getValue()))
                .build();
    }

    public static Tag createJava() {
        return create(TagNameFixture.tagName("Java"));
    }

    public static Tag createSpring() {
        return create(TagNameFixture.tagName("Spring"));
    }

    public static Tag createReact() {
        return create(TagNameFixture.tagName("React"));
    }
}
