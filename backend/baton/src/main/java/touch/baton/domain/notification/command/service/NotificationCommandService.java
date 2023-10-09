package touch.baton.domain.notification.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.command.repository.NotificationCommandRepository;
import touch.baton.domain.notification.exception.NotificationBusinessException;
import touch.baton.domain.member.command.Member;

@RequiredArgsConstructor
@Transactional
@Service
public class NotificationCommandService {

    private final NotificationCommandRepository notificationCommandRepository;

    public void updateNotificationIsReadTrueByMember(final Member member, final Long notificationId) {
        if (getNotificationByNotificationId(notificationId).isNotOwner(member)) {
            throw new NotificationBusinessException("Notification 의 주인(사용자)가 아니므로 알림의 읽은 여부를 수정할 수 없습니다.");
        }

        notificationCommandRepository.updateIsReadTrueByMemberId(notificationId);
    }

    private Notification getNotificationByNotificationId(final Long notificationId) {
        return notificationCommandRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationBusinessException("Notification 식별자값으로 알림을 조회할 수 없습니다."));
    }

    public void deleteNotificationByMember(final Member member, final Long notificationId) {
        if (getNotificationByNotificationId(notificationId).isNotOwner(member)) {
            throw new NotificationBusinessException("Notification 의 주인(사용자)가 아니므로 알림을 삭제할 수 없습니다.");
        }

        notificationCommandRepository.deleteById(notificationId);
    }
}
