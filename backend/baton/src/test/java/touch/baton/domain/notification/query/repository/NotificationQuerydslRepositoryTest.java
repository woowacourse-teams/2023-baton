package touch.baton.domain.notification.query.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.notification.command.Notification;
import touch.baton.fixture.domain.MemberFixture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.NotificationReferencedIdFixture.notificationReferencedId;

class NotificationQuerydslRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private NotificationQuerydslRepository notificationQuerydslRepository;

    @DisplayName("deleted_at 이 null 인 경우의 알림을 조회하기 위해서는 nativeQuery 를 사용한다.")
    @Test
    void success_findAll_deletedAt_isNull() {
        // given
        final Member targetMember = persistRunner(MemberFixture.createHyena()).getMember();

        final Notification deletedAtIsNotNullNotification = persistNotification(targetMember, notificationReferencedId(1L));
        final Notification deletedAtIsNullNotification = persistNotification(targetMember, notificationReferencedId(2L));

        // when
        em.remove(deletedAtIsNullNotification);
        final List<Notification> actual = em.createNativeQuery("select * from notification", Notification.class).getResultList();


        // then
        assertThat(actual).containsExactly(deletedAtIsNotNullNotification, deletedAtIsNullNotification);
    }

    @DisplayName("deleted_at 이 null 이 아닌 경우 알림 목록을 사용자 식별자값을 이용해서 알림 식별자값 기준으로 내림차순 정렬하여 알림 목록을 조회한다")
    @Test
    void findByMemberIdOrderByIdDescLimit() {
        // given
        final Member targetMember = persistRunner(MemberFixture.createHyena()).getMember();

        final List<Notification> savedNotifications = new ArrayList<>();
        for (long referencedId = 1; referencedId <= 20; referencedId++) {
            final Notification savedNotification = persistNotification(targetMember, notificationReferencedId(referencedId));
            savedNotifications.add(savedNotification);
        }
        savedNotifications.sort(orderByNotificationIdDesc());

        // when
        final int limit = 10;
        final List<Notification> actual = notificationQuerydslRepository.findByMemberId(targetMember.getId(), limit);

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
}
