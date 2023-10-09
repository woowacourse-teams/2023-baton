package touch.baton.domain.notification.command.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.fixture.domain.MemberFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.NotificationReferencedIdFixture.notificationReferencedId;

class NotificationCommandRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private NotificationCommandRepository notificationCommandRepository;

    @DisplayName("알림을 삭제할 경우 hard delete 가 아닌 soft delete 로 진행한다.")
    @Test
    void soft_delete() {
        // given
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);

        final Notification notification = persistNotification(runner.getMember(), notificationReferencedId(runnerPost.getId()));

        // when
        notificationCommandRepository.deleteById(notification.getId());

        // then
        final Notification foundNotification = (Notification) em.createNativeQuery("select * from notification where id = :id", Notification.class)
                .setParameter("id", notification.getId())
                .getSingleResult();

        assertThat(foundNotification.getDeletedAt()).isNotNull();
    }

    @DisplayName("알림 읽기 여부를 true 로 업데이트 한다.")
    @Test
    void updateIsReadTrueByMemberId() {
        // given
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);

        final Notification notification = persistNotification(runner.getMember(), notificationReferencedId(runnerPost.getId()));

        // when
        notificationCommandRepository.updateIsReadTrueByMemberId(notification.getId());

        // then
        final Optional<Notification> maybeActual = notificationCommandRepository.findById(notification.getId());

        assertSoftly(softly -> {
            softly.assertThat(maybeActual).isPresent();
            final Notification actual = maybeActual.get();

            assertThat(actual.getIsRead().getValue()).isTrue();
        });
    }
}
