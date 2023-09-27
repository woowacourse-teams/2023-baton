package touch.baton.domain.oauth.query.controller.resolver;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.oauth.command.AuthorizationHeader;
import touch.baton.domain.oauth.command.exception.OauthRequestException;
import touch.baton.infra.auth.jwt.JwtDecoder;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public abstract class UserPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER = "Bearer ";

    private final JwtDecoder jwtDecoder;

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory
    ) {
        final boolean isAuthorizationRequired = isAuthorizationRequired(parameter);
        final boolean isAuthorizationHeaderNotExist = isAuthorizationHeaderNotExist(webRequest);
        if (isAuthorizationRequired && isAuthorizationHeaderNotExist) {
            throw new OauthRequestException(ClientErrorCode.OAUTH_AUTHORIZATION_VALUE_IS_NULL);
        }
        if (!isAuthorizationRequired && isAuthorizationHeaderNotExist) {
            return getGuest();
        }

        final AuthorizationHeader authorization = new AuthorizationHeader(webRequest.getHeader(AUTHORIZATION));
        if (authorization.isNotBearerAuth()) {
            throw new OauthRequestException(ClientErrorCode.OAUTH_AUTHORIZATION_BEARER_TYPE_NOT_FOUND);
        }

        final Claims claims = jwtDecoder.parseAuthorizationHeader(authorization);
        final String socialId = claims.get("socialId", String.class);

        return getUser(socialId);
    }

    private boolean isAuthorizationHeaderNotExist(final NativeWebRequest webRequest) {
        return Objects.isNull(webRequest.getHeader(AUTHORIZATION));
    }

    protected abstract boolean isAuthorizationRequired(final MethodParameter parameter);

    protected abstract Object getGuest();

    protected abstract Object getUser(final String socialId);
}
