package touch.baton.domain.notification.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.notification.command.Notification;

import java.util.List;

public interface NotificationQueryRepository extends JpaRepository<Notification, Long> {

    @Query("""
        select a
        from Notification a
        where a.member.id = :memberId
        order by a.id desc
        limit :limit
        """)
    List<Notification> findByMemberIdLimit(@Param("memberId") final Long memberId, @Param("limit") final int limit);
}
