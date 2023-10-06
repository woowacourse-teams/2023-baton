package touch.baton.fixture.domain;

import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.command.vo.AlarmReferencedId;
import touch.baton.domain.member.command.Member;

import static touch.baton.domain.alarm.command.vo.AlarmType.RUNNER_POST;
import static touch.baton.fixture.vo.AlarmMessageFixture.alarmMessage;
import static touch.baton.fixture.vo.AlarmTitleFixture.alarmTitle;
import static touch.baton.fixture.vo.IsReadFixture.isRead;

public abstract class AlarmFixture {

    private AlarmFixture() {
    }

    public static Alarm create(final Member targetMember, final AlarmReferencedId alarmReferencedId) {
        return Alarm.builder()
                .alarmTitle(alarmTitle("테스트용 알람 제목"))
                .alarmMessage(alarmMessage("테스트용 알람 내용"))
                .alarmType(RUNNER_POST)
                .alarmReferencedId(alarmReferencedId)
                .isRead(isRead(false))
                .member(targetMember)
                .build();
    }
}
