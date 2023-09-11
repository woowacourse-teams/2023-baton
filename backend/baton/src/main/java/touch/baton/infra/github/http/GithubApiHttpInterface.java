package touch.baton.infra.github.http;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import touch.baton.infra.auth.oauth.github.response.GithubMemberResponse;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface GithubApiHttpInterface {

    @GetExchange("https://api.github.com/repos/baton-mission/{}/git/refs/heads/main")
    GithubMemberResponse fetchMember(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}
