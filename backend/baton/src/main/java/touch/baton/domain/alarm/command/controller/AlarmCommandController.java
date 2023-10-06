package touch.baton.domain.alarm.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.alarm.command.service.AlarmCommandService;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.query.controller.resolver.AuthMemberPrincipal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
@RestController
public class AlarmCommandController {

    private final AlarmCommandService alarmCommandService;

    @PatchMapping("/{alarmId}")
    public ResponseEntity<Void> updateAlarmIsReadTrueByAlarmId(@AuthMemberPrincipal final Member member,
                                                              @PathVariable final Long alarmId
    ) {
        alarmCommandService.updateAlarmIsReadTrueByMember(member, alarmId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{alarmId}")
    public ResponseEntity<Void> deleteAlarmByAlarmId(@AuthMemberPrincipal final Member member,
                                                     @PathVariable final Long alarmId
    ) {
        alarmCommandService.deleteAlarmByMember(member, alarmId);

        return ResponseEntity.noContent().build();
    }
}
