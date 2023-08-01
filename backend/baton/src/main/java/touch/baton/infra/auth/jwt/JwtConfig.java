package touch.baton.infra.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt.token")
public record JwtConfig(String secretKey,
                        String issuer
) {
}
