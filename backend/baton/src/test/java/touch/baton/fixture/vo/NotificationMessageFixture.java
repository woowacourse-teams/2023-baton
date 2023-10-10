package touch.baton.fixture.vo;

import touch.baton.domain.notification.command.vo.NotificationMessage;

public abstract class NotificationMessageFixture {

    private NotificationMessageFixture() {
    }

    public static NotificationMessage notificationMessage(final String value) {
        return new NotificationMessage(value);
    }
}
