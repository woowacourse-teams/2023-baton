package touch.baton.assure.common;

import io.jsonwebtoken.Claims;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Profile;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.infra.auth.jwt.JwtDecoder;

import static touch.baton.fixture.vo.AuthorizationHeaderFixture.bearerAuthorizationHeader;

@Profile("test")
@TestComponent
public class JwtTestManager {

    private final JwtDecoder jwtDecoder;

    public JwtTestManager(final JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public SocialId parseToSocialId(final String accessToken) {
        final Claims claims = jwtDecoder.parseAuthorizationHeader(bearerAuthorizationHeader(accessToken));
        final String socialId = claims.get("socialId", String.class);

        return new SocialId(socialId);
    }
}
