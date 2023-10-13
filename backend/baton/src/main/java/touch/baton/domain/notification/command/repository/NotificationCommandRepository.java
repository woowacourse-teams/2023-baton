package touch.baton.domain.notification.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.notification.command.Notification;

public interface NotificationCommandRepository extends JpaRepository<Notification, Long> {
}
