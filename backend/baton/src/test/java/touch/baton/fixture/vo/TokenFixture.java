package touch.baton.fixture.vo;

import touch.baton.tobe.domain.oauth.command.token.Token;

public abstract class TokenFixture {

    private TokenFixture() {
    }

    public static Token token(final String value) {
        return new Token(value);
    }
}
