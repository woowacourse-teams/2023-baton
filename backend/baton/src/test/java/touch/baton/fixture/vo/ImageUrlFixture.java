package touch.baton.fixture.vo;

import touch.baton.domain.member.vo.ImageUrl;

public abstract class ImageUrlFixture {

    private ImageUrlFixture() {
    }

    public static ImageUrl imageUrl(final String value) {
        return new ImageUrl(value);
    }
}
