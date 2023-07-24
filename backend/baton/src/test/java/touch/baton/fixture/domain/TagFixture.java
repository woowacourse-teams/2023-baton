package touch.baton.fixture.domain;

import touch.baton.domain.tag.Tag;

import static touch.baton.fixture.vo.TagCountFixture.tagCount;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

public abstract class TagFixture {

    private TagFixture() {
    }

    public static Tag create(final String tagName, final int tagCount) {
        return Tag.builder()
                .tagName(tagName(tagName))
                .tagCount(tagCount(tagCount))
                .build();
    }

    public static Tag createJava() {
        return create("Java", 1);
    }

    public static Tag createSpring() {
        return create("Spring", 1);
    }

    public static Tag createReact() {
        return create("React", 1);
    }
}
