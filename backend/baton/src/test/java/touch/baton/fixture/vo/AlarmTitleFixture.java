package touch.baton.fixture.vo;

import touch.baton.domain.alarm.command.vo.AlarmTitle;

public abstract class AlarmTitleFixture {

    private AlarmTitleFixture() {
    }

    public static AlarmTitle alarmTitle(final String value) {
        return new AlarmTitle(value);
    }
}
