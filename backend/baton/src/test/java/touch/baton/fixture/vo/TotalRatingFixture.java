package touch.baton.fixture.vo;

import touch.baton.domain.common.vo.TotalRating;

public abstract class TotalRatingFixture {

    private TotalRatingFixture() {
    }

    public static TotalRating totalRating(final int value) {
        return new TotalRating(value);
    }
}
