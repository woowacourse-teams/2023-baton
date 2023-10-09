package touch.baton.domain.notification.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.query.controller.response.NotificationResponses;
import touch.baton.domain.notification.query.service.NotificationQueryService;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.query.controller.resolver.AuthMemberPrincipal;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationQueryController {

    private static final int READ_NOTIFICATION_DEFAULT_LIMIT = 10;

    private final NotificationQueryService notificationQueryService;

    @GetMapping
    public ResponseEntity<NotificationResponses.SimpleNotifications> readNotificationsByMember(@AuthMemberPrincipal final Member member) {
        final List<Notification> foundNotifications = notificationQueryService.readNotificationsByMemberId(member.getId(), READ_NOTIFICATION_DEFAULT_LIMIT);

        return ResponseEntity.ok(NotificationResponses.SimpleNotifications.from(foundNotifications));
    }
}
