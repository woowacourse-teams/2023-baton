package touch.baton.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.controller.response.LoginMemberInfoResponse;
import touch.baton.domain.oauth.controller.resolver.AuthMemberPrincipal;

@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
@RestController
public class MemberProfileController {

    // TODO: 2023/08/09 테스트 코드 작성
    @GetMapping("/me")
    ResponseEntity<LoginMemberInfoResponse> readLoginMemberInfo(@AuthMemberPrincipal final Member member) {
        return ResponseEntity.ok(LoginMemberInfoResponse.from(member));
    }
}
