package touch.baton.domain.alarm.query.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.member.command.Member;
import touch.baton.fixture.domain.MemberFixture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.AlarmReferencedIdFixture.alarmReferencedId;

class AlarmQueryServiceTest extends ServiceTestConfig {

    private AlarmQueryService alarmQueryService;

    @BeforeEach
    void setUp() {
        alarmQueryService = new AlarmQueryService(alarmQueryRepository);
    }

    @DisplayName("사용자 식별자값으로 알람 목록을 조회에 성공한다.")
    @Test
    void success_readAlarmsByMemberId() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());


        final List<Alarm> savedAlarms = new ArrayList<>();
        for (long referencedId = 1; referencedId <= 20; referencedId++) {
            final Alarm savedAlarm = persistAlarm(targetMember, alarmReferencedId(referencedId));
            savedAlarms.add(savedAlarm);
        }
        savedAlarms.sort(orderByAlarmIdDesc());

        // when
        final int limit = 10;
        final List<Alarm> actual = alarmQueryService.readAlarmsByMemberId(targetMember.getId(), limit);

        // then
        final List<Alarm> expected = savedAlarms.subList(0, limit);

        assertSoftly(softly -> {
            softly.assertThat(actual).isSortedAccordingTo(orderByAlarmIdDesc());
            softly.assertThat(actual).containsExactlyElementsOf(expected);
        });
    }

    private Comparator<Alarm> orderByAlarmIdDesc() {
        return (left, right) -> left.getId() < right.getId() ? 1 : -1;
    }

    @DisplayName("사용자 식별자값으로 조회한 알람 목록이 비어있을 경우 빈 목록 반환한다.")
    @Test
    void success_readAlarmsByMemberId_when_alarms_isEmpty() {
        // given
        final Member targetMember = memberCommandRepository.save(MemberFixture.createHyena());

        // when
        final int limit = 10;
        final List<Alarm> actual = alarmQueryService.readAlarmsByMemberId(targetMember.getId(), limit);

        // then
        final List<Alarm> expected = Collections.emptyList();

        assertSoftly(softly -> {
            softly.assertThat(actual).isSortedAccordingTo(orderByAlarmIdDesc());
            softly.assertThat(actual).containsExactlyElementsOf(expected);
        });
    }
}
