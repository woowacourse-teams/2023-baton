package touch.baton.fixture.vo;

import touch.baton.domain.oauth.command.token.AccessToken;

public abstract class AccessTokenFixture {

    private AccessTokenFixture() {
    }

    public static AccessToken accessToken(final String value) {
        return new AccessToken(value);
    }

    public static AccessToken createAccessToken() {
        return accessToken("access-token");
    }
}
