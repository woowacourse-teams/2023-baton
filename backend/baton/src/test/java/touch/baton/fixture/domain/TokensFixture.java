package touch.baton.fixture.domain;

import touch.baton.domain.oauth.command.token.AccessToken;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Tokens;

public abstract class TokensFixture {

    private TokensFixture() {
    }

    public static Tokens create(final AccessToken accessToken, final RefreshToken refreshToken) {
        return new Tokens(accessToken, refreshToken);
    }
}
