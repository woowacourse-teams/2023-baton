package touch.baton.domain.alarm.query.controller.response;

import touch.baton.domain.alarm.command.Alarm;

import java.util.List;

public record AlarmResponses() {

    public record SimpleAlarms(List<AlarmResponse.Simple> data) {

        public static SimpleAlarms from(final List<Alarm> alarms) {
            final List<AlarmResponse.Simple> response = alarms.stream()
                    .map(AlarmResponse.Simple::from)
                    .toList();

            return new SimpleAlarms(response);
        }
    }
}
