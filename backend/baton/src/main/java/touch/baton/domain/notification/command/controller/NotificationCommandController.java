package touch.baton.domain.notification.command.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.notification.command.service.NotificationCommandService;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.query.controller.resolver.AuthMemberPrincipal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationCommandController {

    private final NotificationCommandService notificationCommandService;

    @PatchMapping("/{notificationId}")
    public ResponseEntity<Void> updateNotificationIsReadTrueByNotificationId(@AuthMemberPrincipal final Member member,
                                                                             @PathVariable final Long notificationId
    ) {
        notificationCommandService.updateNotificationIsReadTrueByMember(member, notificationId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotificationByNotificationId(@AuthMemberPrincipal final Member member,
                                                                   @PathVariable final Long notificationId
    ) {
        notificationCommandService.deleteNotificationByMember(member, notificationId);

        return ResponseEntity.noContent().build();
    }
}
