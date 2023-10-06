package touch.baton.domain.alarm.command.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.fixture.domain.MemberFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.AlarmReferencedIdFixture.alarmReferencedId;

class AlarmCommandRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private AlarmCommandRepository alarmCommandRepository;

    @DisplayName("알람을 삭제할 경우 hard delete 가 아닌 soft delete 로 진행한다.")
    @Test
    void soft_delete() {
        // given
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);

        final Alarm alarm = persistAlarm(runner.getMember(), alarmReferencedId(runnerPost.getId()));

        // when
        alarmCommandRepository.deleteById(alarm.getId());

        // then
        final Alarm foundAlarm = (Alarm) em.createNativeQuery("select * from alarm where id = :id", Alarm.class)
                .setParameter("id", alarm.getId())
                .getSingleResult();

        assertThat(foundAlarm.getDeletedAt()).isNotNull();
    }

    @DisplayName("알람 읽기 여부를 true 로 업데이트 한다.")
    @Test
    void updateIsReadTrueByMemberId() {
        // given
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);

        final Alarm alarm = persistAlarm(runner.getMember(), alarmReferencedId(runnerPost.getId()));

        // when
        alarmCommandRepository.updateIsReadTrueByMemberId(alarm.getId());

        // then
        final Optional<Alarm> maybeActual = alarmCommandRepository.findById(alarm.getId());

        assertSoftly(softly -> {
            softly.assertThat(maybeActual).isPresent();
            final Alarm actual = maybeActual.get();

            assertThat(actual.getIsRead().getValue()).isTrue();
        });
    }
}
