package touch.baton.infra.auth.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.Key;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@ConfigurationProperties("jwt.token")
public class JwtConfig {

    private final Key secretKey;
    private final String issuer;

    public JwtConfig(final String secretKey, final String issuer) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(UTF_8));
        this.issuer = issuer;
    }
}
