package touch.baton.fixture.vo;

import touch.baton.domain.supporter.vo.ReviewCount;

public abstract class ReviewCountFixture {

    private ReviewCountFixture() {
    }

    public static ReviewCount reviewCount(final int value) {
        return new ReviewCount(value);
    }
}
