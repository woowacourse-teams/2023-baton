package touch.baton.infra.auth.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@RequiredArgsConstructor
@Component
public class JwtEncoder {

    private final JwtConfig jwtConfig;

    public String jwtToken(final Map<String, Object> payload) {
        final Date now = new Date();

        final JWTCreator.Builder jwtBuilder = JWT.create()
                .withIssuer(jwtConfig.issuer())
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + Duration.ofDays(30).toMillis()));

        payload.forEach((name, value) -> jwtBuilder.withClaim(name, String.valueOf(value)));

        return jwtBuilder.sign(HMAC256(jwtConfig.secretKey()));
    }
}
