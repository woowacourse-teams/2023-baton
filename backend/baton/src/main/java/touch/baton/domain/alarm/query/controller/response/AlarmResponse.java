package touch.baton.domain.alarm.query.controller.response;

import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.command.vo.AlarmType;

import java.time.LocalDateTime;

public record AlarmResponse() {

    public record Simple(Long alarmId,
                         String title,
                         String message,
                         AlarmType alarmType,
                         Long referencedId,
                         boolean isRead,
                         LocalDateTime createdAt
    ) {

        public static Simple from(final Alarm alarm) {
            return new AlarmResponse.Simple(
                    alarm.getId(),
                    alarm.getAlarmTitle().getValue(),
                    alarm.getAlarmMessage().getValue(),
                    alarm.getAlarmType(),
                    alarm.getAlarmReferencedId().getValue(),
                    alarm.getIsRead().getValue(),
                    alarm.getCreatedAt()
            );
        }
    }
}
