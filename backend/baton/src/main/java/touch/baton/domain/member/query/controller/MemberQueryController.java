package touch.baton.domain.member.query.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.controller.response.LoginMemberInfoResponse;
import touch.baton.domain.oauth.query.controller.resolver.AuthMemberPrincipal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
@RestController
public class MemberQueryController {

    @GetMapping("/me")
    ResponseEntity<LoginMemberInfoResponse> readLoginMemberInfo(@AuthMemberPrincipal final Member member) {
        return ResponseEntity.ok(LoginMemberInfoResponse.from(member));
    }
}
