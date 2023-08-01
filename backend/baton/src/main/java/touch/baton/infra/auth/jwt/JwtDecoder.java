package touch.baton.infra.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@RequiredArgsConstructor
@Component
public class JwtDecoder {

    private final JwtConfig jwtConfig;

    public Map<String, Claim> parseJwtToken(final String authHeader) {
        if (Objects.isNull(authHeader)) {
            throw new IllegalStateException("네 맞습니다. 예외 처리해주세요");
        }
        if (!authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("네 맞습니다. 예외 처리해주세요");
        }

        final String token = authHeader.substring("Bearer ".length());
        final DecodedJWT decodedJwt = JWT.require(HMAC256(jwtConfig.secretKey()))
                .build()
                .verify(token);

        return decodedJwt.getClaims();
    }
}
