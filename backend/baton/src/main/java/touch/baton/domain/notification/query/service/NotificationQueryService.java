package touch.baton.domain.notification.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.query.repository.NotificationQueryRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NotificationQueryService {

    private final NotificationQueryRepository notificationQueryRepository;

    public List<Notification> readNotificationsByMemberId(final Long memberId, final int limit) {
        return notificationQueryRepository.findByMemberIdLimit(memberId, limit);
    }
}
