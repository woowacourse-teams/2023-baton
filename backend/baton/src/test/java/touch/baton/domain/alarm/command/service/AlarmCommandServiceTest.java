package touch.baton.domain.alarm.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.command.vo.AlarmReferencedId;
import touch.baton.domain.alarm.exception.AlarmBusinessException;
import touch.baton.domain.member.command.Member;
import touch.baton.fixture.domain.AlarmFixture;
import touch.baton.fixture.domain.MemberFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.AlarmReferencedIdFixture.alarmReferencedId;

class AlarmCommandServiceTest extends ServiceTestConfig {

    private AlarmCommandService alarmCommandService;

    @BeforeEach
    void setUp() {
        alarmCommandService = new AlarmCommandService(alarmCommandRepository);
    }

    @DisplayName("알람 읽은 여부를 true 로 업데이트에 성공한다.")
    @Test
    void success_updateAlarmIsReadByMember() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());
        final AlarmReferencedId alarmReferencedId = alarmReferencedId(1L);
        final Alarm savedAlarm = alarmCommandRepository.save(AlarmFixture.create(targetMember, alarmReferencedId));

        // when
        alarmCommandService.updateAlarmIsReadTrueByMember(targetMember, savedAlarm.getId());

        // then
        final Optional<Alarm> maybeActual = alarmCommandRepository.findById(savedAlarm.getId());

        assertSoftly(softly -> {
            softly.assertThat(maybeActual).isPresent();
            final Alarm actual = maybeActual.get();

            softly.assertThat(actual.getIsRead().getValue()).isEqualTo(true);
        });
    }

    @DisplayName("알람 읽은 여부를 true 로 업데이트 할 때 알람의 주인(사용자)가 아닐 경우 예외가 발생한다.")
    @Test
    void fail_updateAlarmIsReadByMember_when_member_isNotOwner() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());
        final AlarmReferencedId alarmReferencedId = alarmReferencedId(1L);
        final Alarm savedAlarm = alarmCommandRepository.save(AlarmFixture.create(targetMember, alarmReferencedId));

        final Member notOwner = memberCommandRepository.save(MemberFixture.createEthan());

        // when & then
        assertThatThrownBy(() -> alarmCommandService.updateAlarmIsReadTrueByMember(notOwner, savedAlarm.getId()))
                .isInstanceOf(AlarmBusinessException.class);
    }

    @DisplayName("알람 삭제를 성공한다.")
    @Test
    void success_deleteAlarmByMember() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());
        final AlarmReferencedId alarmReferencedId = alarmReferencedId(1L);
        final Alarm savedAlarm = alarmCommandRepository.save(AlarmFixture.create(targetMember, alarmReferencedId));

        // when
        alarmCommandService.deleteAlarmByMember(targetMember, savedAlarm.getId());

        // then
        final Optional<Alarm> actual = alarmCommandRepository.findById(savedAlarm.getId());

        assertThat(actual).isEmpty();
    }

    @DisplayName("알람 삭제을 삭제할 때 알람의 주인(사용자)가 아닐 경우 예외가 발생한다.")
    @Test
    void fail_deleteAlarmByMember_when_member_isNotOwner() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());
        final AlarmReferencedId alarmReferencedId = alarmReferencedId(1L);
        final Alarm savedAlarm = alarmCommandRepository.save(AlarmFixture.create(targetMember, alarmReferencedId));

        final Member notOwner = memberCommandRepository.save(MemberFixture.createEthan());

        // when & then
        assertThatThrownBy(() -> alarmCommandService.deleteAlarmByMember(notOwner, savedAlarm.getId()))
                .isInstanceOf(AlarmBusinessException.class);
    }
}
