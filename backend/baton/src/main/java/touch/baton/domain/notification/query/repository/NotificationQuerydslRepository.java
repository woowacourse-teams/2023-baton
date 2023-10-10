package touch.baton.domain.notification.query.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import touch.baton.domain.notification.command.Notification;

import java.util.List;

import static touch.baton.domain.notification.command.QNotification.notification;

@RequiredArgsConstructor
@Repository
public class NotificationQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Notification> findByMemberId(final Long memberId,
                                             final int limit
    ) {
        return jpaQueryFactory.selectFrom(notification)
                .where(notification.member.id.eq(memberId))
                .orderBy(notification.id.desc())
                .limit(limit)
                .fetch();
    }
}
