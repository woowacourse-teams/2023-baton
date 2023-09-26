package touch.baton.infra.auth.oauth.github.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import touch.baton.infra.auth.oauth.github.GithubOauthConfig;
import touch.baton.infra.auth.oauth.github.http.GithubHttpInterface;
import touch.baton.infra.auth.oauth.github.request.GithubTokenRequest;
import touch.baton.infra.auth.oauth.github.response.GithubMemberResponse;
import touch.baton.infra.auth.oauth.github.token.GithubToken;
import touch.baton.tobe.domain.oauth.command.OauthInformation;
import touch.baton.tobe.domain.oauth.command.OauthType;
import touch.baton.tobe.domain.oauth.command.client.OauthInformationClient;

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
