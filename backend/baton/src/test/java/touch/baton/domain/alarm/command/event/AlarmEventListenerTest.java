package touch.baton.domain.alarm.command.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.command.repository.AlarmCommandRepository;
import touch.baton.domain.alarm.command.vo.AlarmType;
import touch.baton.domain.alarm.query.repository.AlarmQueryRepository;
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
import static touch.baton.domain.alarm.command.vo.AlarmText.MESSAGE_REFERENCED_BY_RUNNER_POST;
import static touch.baton.domain.alarm.command.vo.AlarmText.TITLE_RUNNER_POST_APPLICANT;
import static touch.baton.domain.alarm.command.vo.AlarmText.TITLE_RUNNER_POST_ASSIGN_SUPPORTER;
import static touch.baton.domain.alarm.command.vo.AlarmText.TITLE_RUNNER_POST_REVIEW_STATUS_DONE;

class AlarmEventListenerTest extends RepositoryTestConfig {

    private AlarmEventListener alarmEventListener;

    @Autowired
    private AlarmQueryRepository alarmQueryRepository;

    @BeforeEach
    void setUp(@Autowired AlarmCommandRepository alarmCommandRepository,
               @Autowired RunnerPostQueryRepository runnerPostQueryRepository) {
        alarmEventListener = new AlarmEventListener(alarmCommandRepository, runnerPostQueryRepository);
    }

    @DisplayName("러너 게시글에 서포터가 지원했다는 알람을 생성한다.")
    @Test
    void subscribeRunnerPostApplySupporterEvent() {
        // given
        final Runner targetRunner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(targetRunner);

        // when
        final RunnerPostApplySupporterEvent event = new RunnerPostApplySupporterEvent(runnerPost.getId());
        alarmEventListener.subscribeRunnerPostApplySupporterEvent(event);

        // then
        final List<Alarm> actualAlarms = alarmQueryRepository.findByMemberIdLimit(targetRunner.getMember().getId(), 10);

        final String expectedAlarmTitle = TITLE_RUNNER_POST_APPLICANT.getText();
        final String expectedAlarmMessage = String.format(MESSAGE_REFERENCED_BY_RUNNER_POST.getText(), runnerPost.getTitle().getValue());
        final AlarmType expectedAlarmType = AlarmType.RUNNER_POST;
        final Long expectedReferencedId = runnerPost.getId();
        final boolean expectedIsRead = false;
        final Member expectedMember = targetRunner.getMember();

        assertSoftly(softly -> {
            softly.assertThat(actualAlarms).hasSize(1);
            final Alarm actual = actualAlarms.get(0);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getAlarmTitle().getValue()).isEqualTo(expectedAlarmTitle);
            assertThat(actual.getAlarmMessage().getValue()).isEqualTo(expectedAlarmMessage);
            assertThat(actual.getAlarmType()).isEqualTo(expectedAlarmType);
            assertThat(actual.getAlarmReferencedId().getValue()).isEqualTo(expectedReferencedId);
            assertThat(actual.getIsRead().getValue()).isEqualTo(expectedIsRead);
            assertThat(actual.getMember()).isEqualTo(expectedMember);
        });
    }

    @DisplayName("러너 게시글 리뷰 상태가 DONE 으로 업데이트 되었다는 알람을 생성한다.")
    @Test
    void subscribeRunnerPostReviewStatusDoneEvent() {
        // given
        final Runner targetRunner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(targetRunner);

        // when
        final RunnerPostReviewStatusDoneEvent event = new RunnerPostReviewStatusDoneEvent(runnerPost.getId());
        alarmEventListener.subscribeRunnerPostReviewStatusDoneEvent(event);

        // then
        final List<Alarm> actualAlarms = alarmQueryRepository.findByMemberIdLimit(targetRunner.getMember().getId(), 10);

        final String expectedAlarmTitle = TITLE_RUNNER_POST_REVIEW_STATUS_DONE.getText();
        final String expectedAlarmMessage = String.format(MESSAGE_REFERENCED_BY_RUNNER_POST.getText(), runnerPost.getTitle().getValue());
        final AlarmType expectedAlarmType = AlarmType.RUNNER_POST;
        final Long expectedReferencedId = runnerPost.getId();
        final boolean expectedIsRead = false;
        final Member expectedMember = targetRunner.getMember();

        assertSoftly(softly -> {
            softly.assertThat(actualAlarms).hasSize(1);
            final Alarm actual = actualAlarms.get(0);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getAlarmTitle().getValue()).isEqualTo(expectedAlarmTitle);
            assertThat(actual.getAlarmMessage().getValue()).isEqualTo(expectedAlarmMessage);
            assertThat(actual.getAlarmType()).isEqualTo(expectedAlarmType);
            assertThat(actual.getAlarmReferencedId().getValue()).isEqualTo(expectedReferencedId);
            assertThat(actual.getIsRead().getValue()).isEqualTo(expectedIsRead);
            assertThat(actual.getMember()).isEqualTo(expectedMember);
        });
    }

    @DisplayName("러너 게시글에 서포터를 할당했다는 알람을 생성한다.")
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
        alarmEventListener.subscribeRunnerPostAssignSupporterEvent(event);

        // then
        final List<Alarm> actualAlarms = alarmQueryRepository.findByMemberIdLimit(targetSupporter.getMember().getId(), 10);

        final String expectedAlarmTitle = TITLE_RUNNER_POST_ASSIGN_SUPPORTER.getText();
        final String expectedAlarmMessage = String.format(MESSAGE_REFERENCED_BY_RUNNER_POST.getText(), runnerPost.getTitle().getValue());
        final AlarmType expectedAlarmType = AlarmType.RUNNER_POST;
        final Long expectedReferencedId = runnerPost.getId();
        final boolean expectedIsRead = false;
        final Member expectedMember = targetSupporter.getMember();

        assertSoftly(softly -> {
            softly.assertThat(actualAlarms).hasSize(1);
            final Alarm actual = actualAlarms.get(0);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getAlarmTitle().getValue()).isEqualTo(expectedAlarmTitle);
            assertThat(actual.getAlarmMessage().getValue()).isEqualTo(expectedAlarmMessage);
            assertThat(actual.getAlarmType()).isEqualTo(expectedAlarmType);
            assertThat(actual.getAlarmReferencedId().getValue()).isEqualTo(expectedReferencedId);
            assertThat(actual.getIsRead().getValue()).isEqualTo(expectedIsRead);
            assertThat(actual.getMember()).isEqualTo(expectedMember);
        });
    }
}
