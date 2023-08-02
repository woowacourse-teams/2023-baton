package touch.baton.infra.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JwtDecoder {

    private final JwtConfig jwtConfig;

    public Claims parseJwtToken(final String authHeader) {
        if (Objects.isNull(authHeader)) {
            throw new IllegalStateException("네 맞습니다. 예외 처리해주세요");
        }
        if (!authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("네 맞습니다. 예외 처리해주세요");
        }

        final String token = authHeader.substring("Bearer ".length());
        final JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey())
                .requireIssuer(jwtConfig.getIssuer())
                .build();

        return jwtParser.parseClaimsJws(token).getBody();
    }
}
