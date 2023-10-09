package touch.baton.domain.notification.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.notification.command.Notification;

public interface NotificationCommandRepository extends JpaRepository<Notification, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        update Notification n
        set n.isRead.value = true
        where n.id = :notificationId
        """)
    void updateIsReadTrueByMemberId(@Param("notificationId") Long notificationId);
}
