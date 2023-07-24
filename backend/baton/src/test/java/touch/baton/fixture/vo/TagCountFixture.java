package touch.baton.fixture.vo;

import touch.baton.domain.tag.vo.TagCount;

public abstract class TagCountFixture {

    private TagCountFixture() {
    }

    public static TagCount tagCount(final int value) {
        return new TagCount(value);
    }
}
