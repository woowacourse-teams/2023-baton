package touch.baton.fixture.domain;

import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.command.vo.NotificationReferencedId;
import touch.baton.domain.member.command.Member;

import static touch.baton.domain.notification.command.vo.NotificationType.RUNNER_POST;
import static touch.baton.fixture.vo.NotificationMessageFixture.notificationMessage;
import static touch.baton.fixture.vo.NotificationTitleFixture.notificationTitle;
import static touch.baton.fixture.vo.IsReadFixture.isRead;

public abstract class NotificationFixture {

    private NotificationFixture() {
    }

    public static Notification create(final Member targetMember, final NotificationReferencedId notificationReferencedId) {
        return Notification.builder()
                .notificationTitle(notificationTitle("테스트용 알림 제목"))
                .notificationMessage(notificationMessage("테스트용 알림 내용"))
                .notificationType(RUNNER_POST)
                .notificationReferencedId(notificationReferencedId)
                .isRead(isRead(false))
                .member(targetMember)
                .build();
    }
}
