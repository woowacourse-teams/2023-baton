package touch.baton.infra.auth.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class JwtEncoder {

    private final JwtConfig jwtConfig;

    public String jwtToken(final Map<String, Object> payload) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + Duration.ofDays(30).toMillis());
        final Claims claims = Jwts.claims();

        final JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(jwtConfig.getSecretKey(), SignatureAlgorithm.HS256)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .addClaims(claims)
                .addClaims(payload);

        return jwtBuilder.compact();
    }
}
