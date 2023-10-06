package touch.baton.fixture.vo;

import touch.baton.domain.alarm.command.vo.AlarmReferencedId;

public abstract class AlarmReferencedIdFixture {

    private AlarmReferencedIdFixture() {
    }

    public static AlarmReferencedId alarmReferencedId(final Long value) {
        return new AlarmReferencedId(value);
    }
}
