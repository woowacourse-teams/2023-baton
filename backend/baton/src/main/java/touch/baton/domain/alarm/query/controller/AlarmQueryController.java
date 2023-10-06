package touch.baton.domain.alarm.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.query.controller.response.AlarmResponses;
import touch.baton.domain.alarm.query.service.AlarmQueryService;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.query.controller.resolver.AuthMemberPrincipal;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
@RestController
public class AlarmQueryController {

    private static final int READ_ALARM_DEFAULT_LIMIT = 10;

    private final AlarmQueryService alarmQueryService;

    @GetMapping
    public ResponseEntity<AlarmResponses.SimpleAlarms> readAlarmsByMember(@AuthMemberPrincipal final Member member) {
        final List<Alarm> foundAlarms = alarmQueryService.readAlarmsByMemberId(member.getId(), READ_ALARM_DEFAULT_LIMIT);

        return ResponseEntity.ok(AlarmResponses.SimpleAlarms.from(foundAlarms));
    }
}
