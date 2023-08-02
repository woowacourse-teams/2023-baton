package touch.baton.infra.auth.oauth.github.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import touch.baton.domain.oauth.OauthInformation;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.client.OauthInformationClient;
import touch.baton.infra.auth.oauth.github.http.GithubHttpInterface;
import touch.baton.infra.auth.oauth.github.request.GithubTokenRequest;
import touch.baton.infra.auth.oauth.github.response.GithubMemberResponse;
import touch.baton.infra.auth.oauth.github.token.GithubToken;
import touch.baton.infra.auth.oauth.github.GithubOauthConfig;

@RequiredArgsConstructor
@Component
public class GithubInformationClient implements OauthInformationClient {

    private final GithubHttpInterface githubHttpInterface;
    private final GithubOauthConfig githubOauthConfig;

    @Override
    public OauthType oauthType() {
        return OauthType.GITHUB;
    }

    @Override
    public OauthInformation fetchInformation(final String authCode) {
        final GithubToken githubToken = githubHttpInterface.fetchToken(tokenRequestBody(authCode));
        final GithubMemberResponse githubMemberResponse = githubHttpInterface.fetchMember("Bearer " + githubToken.accessToken());

        return githubMemberResponse.toOauthInformation(githubToken.accessToken());
    }

    private GithubTokenRequest tokenRequestBody(final String authCode) {
        return new GithubTokenRequest(
                githubOauthConfig.clientId(),
                githubOauthConfig.clientSecret(),
                authCode,
                githubOauthConfig.redirectUri());
    }
}
