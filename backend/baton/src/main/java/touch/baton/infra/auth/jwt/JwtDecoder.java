package touch.baton.infra.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.oauth.exception.OauthRequestException;

@Profile("!test")
@RequiredArgsConstructor
@Component
public class JwtDecoder {

    private static final String BEARER = "Bearer ";

    private final JwtConfig jwtConfig;

    public Claims parseJwtToken(final String authHeader) {
        try {
            final JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .requireIssuer(jwtConfig.getIssuer())
                    .build();

            final String token = parseBearerAccessToken(authHeader);
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (final SignatureException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_SIGNATURE_IS_WRONG);
        } catch (final ExpiredJwtException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_CLAIM_IS_ALREADY_EXPIRED);
        } catch (final MalformedJwtException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_FORM_IS_WRONG);
        } catch (final MissingClaimException | IncorrectClaimException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_CLAIM_IS_WRONG);
        }
    }

    public Claims parseExpiredJwtToken(final String authHeader) {
        try {
            final JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .requireIssuer(jwtConfig.getIssuer())
                    .build();

            final String token = parseBearerAccessToken(authHeader);
            jwtParser.parseClaimsJws(token).getBody();

            throw new OauthRequestException(ClientErrorCode.JWT_CLAIM_IS_NOT_EXPIRED);
        } catch (final SignatureException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_SIGNATURE_IS_WRONG);
        } catch (final MalformedJwtException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_FORM_IS_WRONG);
        } catch (final MissingClaimException | IncorrectClaimException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_CLAIM_IS_WRONG);
        } catch (final ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private String parseBearerAccessToken(final String authHeader) {
        return authHeader.substring(BEARER.length());
    }
}
