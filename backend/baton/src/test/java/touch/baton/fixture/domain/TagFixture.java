package touch.baton.fixture.domain;

import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagCount;
import touch.baton.fixture.vo.TagNameFixture;

import static touch.baton.fixture.vo.TagCountFixture.tagCount;

public abstract class TagFixture {

    private TagFixture() {
    }

    public static Tag create(final TagName tagName, final TagCount tagCount) {
        return Tag.builder()
                .tagName(tagName)
                .tagCount(tagCount)
                .build();
    }

    public static Tag createJava() {
        return create(TagNameFixture.tagName("Java"), tagCount(1));
    }

    public static Tag createSpring() {
        return create(TagNameFixture.tagName("Spring"), tagCount(1));
    }

    public static Tag createReact() {
        return create(TagNameFixture.tagName("React"), tagCount(1));
    }
}
