package touch.baton.infra.auth.oauth.github.authcode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.authcode.AuthCodeRequestUrlProvider;
import touch.baton.infra.auth.oauth.github.GithubOauthConfig;

@RequiredArgsConstructor
@Component
public class GithubAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final GithubOauthConfig githubOauthConfig;

    @Override
    public OauthType oauthServer() {
        return OauthType.GITHUB;
    }

    @Override
    public String getRequestUrl() {
        return UriComponentsBuilder
                .fromUriString("https://github.com/login/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", githubOauthConfig.clientId())
                .queryParam("redirect_uri", githubOauthConfig.redirectUri())
                .queryParam("scope", String.join(",", githubOauthConfig.scope()))
                .toUriString();
    }
}
