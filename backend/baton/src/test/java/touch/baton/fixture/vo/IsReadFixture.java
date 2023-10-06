package touch.baton.fixture.vo;

import touch.baton.domain.alarm.command.vo.IsRead;

public abstract class IsReadFixture {

    private IsReadFixture() {
    }

    public static IsRead isRead(final boolean value) {
        return new IsRead(value);
    }
}
