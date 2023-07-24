package touch.baton.fixture.vo;

import touch.baton.domain.common.vo.ChattingCount;

public abstract class ChattingCountFixture {

    private ChattingCountFixture() {
    }

    public static ChattingCount chattingCount(final int value) {
        return new ChattingCount(value);
    }
}
