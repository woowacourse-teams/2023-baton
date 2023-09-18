package touch.baton.fixture.vo;

import touch.baton.domain.oauth.AuthorizationHeader;

public class AuthorizationHeaderFixture {

    private static final String BEARER = "Bearer ";

    private AuthorizationHeaderFixture() {
    }

    public static AuthorizationHeader authorizationHeader(final String authorizationHeader) {
        return new AuthorizationHeader(authorizationHeader);
    }

    public static AuthorizationHeader bearerAuthorizationHeader(final String authorizationHeader) {
        return new AuthorizationHeader(BEARER + authorizationHeader);
    }
}
