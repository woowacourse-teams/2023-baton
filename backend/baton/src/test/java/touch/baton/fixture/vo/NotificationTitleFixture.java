package touch.baton.fixture.vo;

import touch.baton.domain.notification.command.vo.NotificationTitle;

public abstract class NotificationTitleFixture {

    private NotificationTitleFixture() {
    }

    public static NotificationTitle notificationTitle(final String value) {
        return new NotificationTitle(value);
    }
}
