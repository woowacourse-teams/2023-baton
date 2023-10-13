package touch.baton.fixture.vo;

import touch.baton.domain.runnerpost.command.vo.WatchedCount;

public abstract class WatchedCountFixture {

    private WatchedCountFixture() {
    }

    public static WatchedCount watchedCount(final int value) {
        return new WatchedCount(value);
    }
}
