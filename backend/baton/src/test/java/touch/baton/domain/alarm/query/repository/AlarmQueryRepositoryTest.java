package touch.baton.domain.alarm.query.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.member.command.Member;
import touch.baton.fixture.domain.MemberFixture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.AlarmReferencedIdFixture.alarmReferencedId;

class AlarmQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private AlarmQueryRepository alarmQueryRepository;

    @DisplayName("deleted_at 이 null 이 아닌 모든 알람을 조회한다.")
    @Test
    void success_findAll_without_deletedAt_isNotNull() {
        // given
        final Member targetMember = persistRunner(MemberFixture.createHyena()).getMember();

        final Alarm notExpected = persistAlarm(targetMember, alarmReferencedId(1L));
        final Alarm expected = persistAlarm(targetMember, alarmReferencedId(2L));

        // when
        em.remove(notExpected);
        final List<Alarm> actual = alarmQueryRepository.findAll();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).containsExactly(expected);
            softly.assertThat(actual).doesNotContain(notExpected);
        });
    }

    @DisplayName("deleted_at 이 null 인 경우의 알람을 조회하기 위해서는 nativeQuery 를 사용한다.")
    @Test
    void success_findAll_deletedAt_isNull() {
        // given
        final Member targetMember = persistRunner(MemberFixture.createHyena()).getMember();

        final Alarm deletedAtIsNotNullAlarm = persistAlarm(targetMember, alarmReferencedId(1L));
        final Alarm deletedAtIsNullAlarm = persistAlarm(targetMember, alarmReferencedId(2L));

        // when
        em.remove(deletedAtIsNullAlarm);
        final List<Alarm> actual = em.createNativeQuery("select * from alarm", Alarm.class).getResultList();


        // then
        assertThat(actual).containsExactly(deletedAtIsNotNullAlarm, deletedAtIsNullAlarm);
    }

    @DisplayName("deleted_at 이 null 이 아닌 경우 알람 목록을 사용자 식별자값을 이용해서 알람 식별자값 기준으로 내림차순 정렬하여 알람 목록을 조회한다")
    @Test
    void findByMemberIdOrderByIdDescLimit() {
        // given
        final Member targetMember = persistRunner(MemberFixture.createHyena()).getMember();

        final List<Alarm> savedAlarms = new ArrayList<>();
        for (long referencedId = 1; referencedId <= 20; referencedId++) {
            final Alarm savedAlarm = persistAlarm(targetMember, alarmReferencedId(referencedId));
            savedAlarms.add(savedAlarm);
        }
        savedAlarms.sort(orderByAlarmIdDesc());

        // when
        final int limit = 10;
        final List<Alarm> actual = alarmQueryRepository.findByMemberIdLimit(targetMember.getId(), limit);

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
}
