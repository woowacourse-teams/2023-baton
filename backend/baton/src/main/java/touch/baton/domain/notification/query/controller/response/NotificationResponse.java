package touch.baton.domain.notification.query.controller.response;

import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.command.vo.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse() {

    public record Simple(Long notificationId,
                         String title,
                         String message,
                         NotificationType notificationType,
                         Long referencedId,
                         boolean isRead,
                         LocalDateTime createdAt
    ) {

        public static Simple from(final Notification notification) {
            return new NotificationResponse.Simple(
                    notification.getId(),
                    notification.getNotificationTitle().getValue(),
                    notification.getNotificationMessage().getValue(),
                    notification.getNotificationType(),
                    notification.getNotificationReferencedId().getValue(),
                    notification.getIsRead().getValue(),
                    notification.getCreatedAt()
            );
        }
    }
}
