package touch.baton.domain.notification.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.command.vo.NotificationReferencedId;
import touch.baton.domain.notification.exception.NotificationBusinessException;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.notification.exception.NotificationDomainException;
import touch.baton.fixture.domain.NotificationFixture;
import touch.baton.fixture.domain.MemberFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.NotificationReferencedIdFixture.notificationReferencedId;

class NotificationCommandServiceTest extends ServiceTestConfig {

    private NotificationCommandService notificationCommandService;

    @BeforeEach
    void setUp() {
        notificationCommandService = new NotificationCommandService(notificationCommandRepository);
    }

    @DisplayName("알림 읽은 여부를 true 로 업데이트에 성공한다.")
    @Test
    void success_updateNotificationIsReadByMember() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());
        final NotificationReferencedId notificationReferencedId = notificationReferencedId(1L);
        final Notification savedNotification = notificationCommandRepository.save(NotificationFixture.create(targetMember, notificationReferencedId));

        // when
        notificationCommandService.updateNotificationIsReadTrueByMember(targetMember, savedNotification.getId());

        // then
        final Optional<Notification> maybeActual = notificationCommandRepository.findById(savedNotification.getId());

        assertSoftly(softly -> {
            softly.assertThat(maybeActual).isPresent();
            final Notification actual = maybeActual.get();

            softly.assertThat(actual.getIsRead().getValue()).isEqualTo(true);
        });
    }

    @DisplayName("알림 읽은 여부를 읽음 상태로 업데이트 할 때 알림의 주인(사용자)가 아닐 경우 예외가 발생한다.")
    @Test
    void fail_updateNotificationIsReadByMember_when_member_isNotOwner() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());
        final NotificationReferencedId notificationReferencedId = notificationReferencedId(1L);
        final Notification savedNotification = notificationCommandRepository.save(NotificationFixture.create(targetMember, notificationReferencedId));

        final Member notOwner = memberCommandRepository.save(MemberFixture.createEthan());

        // when & then
        assertThatThrownBy(() -> notificationCommandService.updateNotificationIsReadTrueByMember(notOwner, savedNotification.getId()))
                .isInstanceOf(NotificationDomainException.class);
    }

    @DisplayName("알림 삭제를 성공한다.")
    @Test
    void success_deleteNotificationByMember() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());
        final NotificationReferencedId notificationReferencedId = notificationReferencedId(1L);
        final Notification savedNotification = notificationCommandRepository.save(NotificationFixture.create(targetMember, notificationReferencedId));

        // when
        notificationCommandService.deleteNotificationByMember(targetMember, savedNotification.getId());

        // then
        final Optional<Notification> actual = notificationCommandRepository.findById(savedNotification.getId());

        assertThat(actual).isEmpty();
    }

    @DisplayName("알림 삭제을 삭제할 때 알림의 주인(사용자)가 아닐 경우 예외가 발생한다.")
    @Test
    void fail_deleteNotificationByMember_when_member_isNotOwner() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());
        final NotificationReferencedId notificationReferencedId = notificationReferencedId(1L);
        final Notification savedNotification = notificationCommandRepository.save(NotificationFixture.create(targetMember, notificationReferencedId));

        final Member notOwner = memberCommandRepository.save(MemberFixture.createEthan());

        // when & then
        assertThatThrownBy(() -> notificationCommandService.deleteNotificationByMember(notOwner, savedNotification.getId()))
                .isInstanceOf(NotificationBusinessException.class);
    }
}
