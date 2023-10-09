package touch.baton.fixture.vo;

import touch.baton.domain.notification.command.vo.IsRead;

public abstract class IsReadFixture {

    private IsReadFixture() {
    }

    public static IsRead isRead(final boolean value) {
        return new IsRead(value);
    }
}
