package touch.baton.fixture.vo;

import touch.baton.domain.alarm.command.vo.AlarmMessage;

public abstract class AlarmMessageFixture {

    private AlarmMessageFixture() {
    }

    public static AlarmMessage alarmMessage(final String value) {
        return new AlarmMessage(value);
    }
}
