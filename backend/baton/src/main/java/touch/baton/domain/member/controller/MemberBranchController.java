package touch.baton.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.service.dto.GithubBranchManageable;
import touch.baton.domain.member.service.dto.GithubRepoNameRequest;
import touch.baton.domain.oauth.controller.resolver.AuthMemberPrincipal;

import java.net.URI;

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
