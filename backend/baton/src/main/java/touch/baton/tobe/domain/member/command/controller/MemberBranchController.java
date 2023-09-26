package touch.baton.tobe.domain.member.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.service.GithubBranchManageable;
import touch.baton.tobe.domain.member.command.service.dto.GithubRepoNameRequest;
import touch.baton.tobe.domain.oauth.query.controller.resolver.AuthMemberPrincipal;

import java.net.URI;

// FIXME: 2023/09/26 패키지 위치 변경
@RequiredArgsConstructor
@RequestMapping("/api/v1/branch")
@RestController
public class MemberBranchController {

    private final GithubBranchManageable githubBranchManageable;

    @PostMapping
    public ResponseEntity<Void> createMemberBranch(@AuthMemberPrincipal final Member member,
                                                   @Valid @RequestBody final GithubRepoNameRequest githubRepoNameRequest
    ) {
        githubBranchManageable.createBranch(githubRepoNameRequest.repoName(), member.getSocialId().getValue());
        final URI redirectUri = URI.create("/api/v1/profile/me");
        return ResponseEntity.created(redirectUri).build();
    }
}
