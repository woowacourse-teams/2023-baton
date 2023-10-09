package touch.baton.domain.notification.query.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.member.command.Member;
import touch.baton.fixture.domain.MemberFixture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.NotificationReferencedIdFixture.notificationReferencedId;

class NotificationQueryServiceTest extends ServiceTestConfig {

    private NotificationQueryService notificationQueryService;

    @BeforeEach
    void setUp() {
        notificationQueryService = new NotificationQueryService(notificationQueryRepository);
    }

    @DisplayName("사용자 식별자값으로 알림 목록을 조회에 성공한다.")
    @Test
    void success_readNotificationsByMemberId() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());


        final List<Notification> savedNotifications = new ArrayList<>();
        for (long referencedId = 1; referencedId <= 20; referencedId++) {
            final Notification savedNotification = persistNotification(targetMember, notificationReferencedId(referencedId));
            savedNotifications.add(savedNotification);
        }
        savedNotifications.sort(orderByNotificationIdDesc());

        // when
        final int limit = 10;
        final List<Notification> actual = notificationQueryService.readNotificationsByMemberId(targetMember.getId(), limit);

        // then
        final List<Notification> expected = savedNotifications.subList(0, limit);

        assertSoftly(softly -> {
            softly.assertThat(actual).isSortedAccordingTo(orderByNotificationIdDesc());
            softly.assertThat(actual).containsExactlyElementsOf(expected);
        });
    }

    private Comparator<Notification> orderByNotificationIdDesc() {
        return (left, right) -> left.getId() < right.getId() ? 1 : -1;
    }

    @DisplayName("사용자 식별자값으로 조회한 알림 목록이 비어있을 경우 빈 목록 반환한다.")
    @Test
    void success_readNotificationsByMemberId_when_Notifications_isEmpty() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());

        // when
        final int limit = 10;
        final List<Notification> actual = notificationQueryService.readNotificationsByMemberId(targetMember.getId(), limit);

        // then
        final List<Notification> expected = Collections.emptyList();

        assertSoftly(softly -> {
            softly.assertThat(actual).isSortedAccordingTo(orderByNotificationIdDesc());
            softly.assertThat(actual).containsExactlyElementsOf(expected);
        });
    }
}
