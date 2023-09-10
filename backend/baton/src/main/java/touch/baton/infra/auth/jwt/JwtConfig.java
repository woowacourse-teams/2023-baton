package touch.baton.infra.auth.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.Key;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@ConfigurationProperties("jwt.token")
public class JwtConfig {

    private final String secretKey;
    private final String issuer;
    private final int expireMinutes;

    public Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8));
    }

    public String getIssuer() {
        return this.issuer;
    }

    public int getExpireMinutes() {
        return expireMinutes;
    }
}
