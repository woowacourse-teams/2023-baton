package touch.baton.fixture.vo;

import touch.baton.domain.notification.command.vo.NotificationReferencedId;

public abstract class NotificationReferencedIdFixture {

    private NotificationReferencedIdFixture() {
    }

    public static NotificationReferencedId notificationReferencedId(final Long value) {
        return new NotificationReferencedId(value);
    }
}
