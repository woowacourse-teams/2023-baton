package touch.baton.infra.oauth.github.http;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import touch.baton.infra.oauth.github.request.GithubTokenRequest;
import touch.baton.infra.oauth.github.response.GithubMemberResponse;
import touch.baton.infra.oauth.github.token.GithubToken;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface GithubHttpInterface {

    @PostExchange(url = "https://github.com/login/oauth/access_token", accept = APPLICATION_JSON_VALUE)
    GithubToken fetchToken(@RequestBody GithubTokenRequest request);

    @GetExchange("https://api.github.com/user")
    GithubMemberResponse fetchMember(@RequestHeader(name = AUTHORIZATION) String bearerToken);}
