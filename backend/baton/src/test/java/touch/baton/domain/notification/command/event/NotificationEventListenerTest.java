package touch.baton.domain.notification.command.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.command.repository.NotificationCommandRepository;
import touch.baton.domain.notification.command.vo.NotificationType;
import touch.baton.domain.notification.query.repository.NotificationQueryRepository;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.event.RunnerPostApplySupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostAssignSupporterEvent;
import touch.baton.domain.runnerpost.command.event.RunnerPostReviewStatusDoneEvent;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;
import touch.baton.fixture.domain.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.domain.notification.command.vo.NotificationText.MESSAGE_REFERENCED_BY_RUNNER_POST;
import static touch.baton.domain.notification.command.vo.NotificationText.TITLE_RUNNER_POST_APPLICANT;
import static touch.baton.domain.notification.command.vo.NotificationText.TITLE_RUNNER_POST_ASSIGN_SUPPORTER;
import static touch.baton.domain.notification.command.vo.NotificationText.TITLE_RUNNER_POST_REVIEW_STATUS_DONE;

class NotificationEventListenerTest extends RepositoryTestConfig {

    private NotificationEventListener notificationEventListener;

    @Autowired
    private NotificationQueryRepository notificationQueryRepository;

    @BeforeEach
    void setUp(@Autowired NotificationCommandRepository notificationCommandRepository,
               @Autowired RunnerPostQueryRepository runnerPostQueryRepository
    ) {
        notificationEventListener = new NotificationEventListener(notificationCommandRepository, runnerPostQueryRepository);
    }

    @DisplayName("러너 게시글에 서포터가 지원했다는 알림을 생성한다.")
    @Test
    void subscribeRunnerPostApplySupporterEvent() {
        // given
        final Runner targetRunner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(targetRunner);

        // when
        final RunnerPostApplySupporterEvent event = new RunnerPostApplySupporterEvent(runnerPost.getId());
        notificationEventListener.subscribeRunnerPostApplySupporterEvent(event);

        // then
        final List<Notification> actualNotifications = notificationQueryRepository.findByMemberIdLimit(targetRunner.getMember().getId(), 10);

        final String expectedNotificationTitle = "서포터의 제안이 왔습니다.";
        final String expectedNotificationMessage = String.format("관련 게시글 - %s", runnerPost.getTitle().getValue());
        final NotificationType expectedNotificationType = NotificationType.RUNNER_POST;
        final Long expectedReferencedId = runnerPost.getId();
        final boolean expectedIsRead = false;
        final Member expectedMember = targetRunner.getMember();

        assertSoftly(softly -> {
            softly.assertThat(actualNotifications).hasSize(1);
            final Notification actual = actualNotifications.get(0);

            softly.assertThat(actual.getId()).isPositive();
            softly.assertThat(actual.getNotificationTitle().getValue()).isEqualTo(expectedNotificationTitle);
            softly.assertThat(actual.getNotificationMessage().getValue()).isEqualTo(expectedNotificationMessage);
            softly.assertThat(actual.getNotificationType()).isEqualTo(expectedNotificationType);
            softly.assertThat(actual.getNotificationReferencedId().getValue()).isEqualTo(expectedReferencedId);
            softly.assertThat(actual.getIsRead().getValue()).isEqualTo(expectedIsRead);
            softly.assertThat(actual.getMember()).isEqualTo(expectedMember);
        });
    }

    @DisplayName("러너 게시글 리뷰 상태가 DONE 으로 업데이트 되었다는 알림을 생성한다.")
    @Test
    void subscribeRunnerPostReviewStatusDoneEvent() {
        // given
        final Runner targetRunner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(targetRunner);

        // when
        final RunnerPostReviewStatusDoneEvent event = new RunnerPostReviewStatusDoneEvent(runnerPost.getId());
        notificationEventListener.subscribeRunnerPostReviewStatusDoneEvent(event);

        // then
        final List<Notification> actualNotifications = notificationQueryRepository.findByMemberIdLimit(targetRunner.getMember().getId(), 10);

        final String expectedNotificationTitle = "코드 리뷰 상태가 완료로 변경되었습니다.";
        final String expectedNotificationMessage = String.format("관련 게시글 - %s", runnerPost.getTitle().getValue());
        final NotificationType expectedNotificationType = NotificationType.RUNNER_POST;
        final Long expectedReferencedId = runnerPost.getId();
        final boolean expectedIsRead = false;
        final Member expectedMember = targetRunner.getMember();

        assertSoftly(softly -> {
            softly.assertThat(actualNotifications).hasSize(1);
            final Notification actual = actualNotifications.get(0);

            softly.assertThat(actual.getId()).isPositive();
            softly.assertThat(actual.getNotificationTitle().getValue()).isEqualTo(expectedNotificationTitle);
            softly.assertThat(actual.getNotificationMessage().getValue()).isEqualTo(expectedNotificationMessage);
            softly.assertThat(actual.getNotificationType()).isEqualTo(expectedNotificationType);
            softly.assertThat(actual.getNotificationReferencedId().getValue()).isEqualTo(expectedReferencedId);
            softly.assertThat(actual.getIsRead().getValue()).isEqualTo(expectedIsRead);
            softly.assertThat(actual.getMember()).isEqualTo(expectedMember);
        });
    }

    @DisplayName("러너 게시글에 서포터를 할당했다는 알림을 생성한다.")
    @Test
    void subscribeRunnerPostAssignSupporterEvent() {
        // given
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final Supporter targetSupporter = persistSupporter(MemberFixture.createEthan());

        persistApplicant(targetSupporter, runnerPost);
        persistAssignSupporter(targetSupporter, runnerPost);

        // when
        final RunnerPostAssignSupporterEvent event = new RunnerPostAssignSupporterEvent(runnerPost.getId());
        notificationEventListener.subscribeRunnerPostAssignSupporterEvent(event);

        // then
        final List<Notification> actualNotifications = notificationQueryRepository.findByMemberIdLimit(targetSupporter.getMember().getId(), 10);

        final String expectedNotificationTitle = "코드 리뷰 매칭이 완료되었습니다.";
        final String expectedNotificationMessage = String.format("관련 게시글 - %s", runnerPost.getTitle().getValue());
        final NotificationType expectedNotificationType = NotificationType.RUNNER_POST;
        final Long expectedReferencedId = runnerPost.getId();
        final boolean expectedIsRead = false;
        final Member expectedMember = targetSupporter.getMember();

        assertSoftly(softly -> {
            softly.assertThat(actualNotifications).hasSize(1);
            final Notification actual = actualNotifications.get(0);

            softly.assertThat(actual.getId()).isPositive();
            softly.assertThat(actual.getNotificationTitle().getValue()).isEqualTo(expectedNotificationTitle);
            softly.assertThat(actual.getNotificationMessage().getValue()).isEqualTo(expectedNotificationMessage);
            softly.assertThat(actual.getNotificationType()).isEqualTo(expectedNotificationType);
            softly.assertThat(actual.getNotificationReferencedId().getValue()).isEqualTo(expectedReferencedId);
            softly.assertThat(actual.getIsRead().getValue()).isEqualTo(expectedIsRead);
            softly.assertThat(actual.getMember()).isEqualTo(expectedMember);
        });
    }
}
