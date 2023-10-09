package touch.baton.domain.notification.query.controller.response;

import touch.baton.domain.notification.command.Notification;

import java.util.List;

public record NotificationResponses() {

    public record SimpleNotifications(List<NotificationResponse.Simple> data) {

        public static SimpleNotifications from(final List<Notification> notifications) {
            final List<NotificationResponse.Simple> response = notifications.stream()
                    .map(NotificationResponse.Simple::from)
                    .toList();

            return new SimpleNotifications(response);
        }
    }
}
