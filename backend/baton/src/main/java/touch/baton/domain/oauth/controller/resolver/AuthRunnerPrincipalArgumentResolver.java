package touch.baton.domain.oauth.controller.resolver;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.oauth.exception.OauthRequestException;
import touch.baton.domain.oauth.repository.OauthRunnerRepository;
import touch.baton.domain.runner.Runner;
import touch.baton.infra.auth.jwt.JwtDecoder;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class AuthRunnerPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER = "Bearer ";

    private final JwtDecoder jwtDecoder;
    private final OauthRunnerRepository oauthRunnerRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthRunnerPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory
    ) throws Exception {
        final String authHeader = webRequest.getHeader(AUTHORIZATION);

        // FIXME: 2023/08/03 null 말고 GUEST, USER, ADMIN role 추가해야할 것 같아요.
        if (!Objects.requireNonNull(parameter.getParameterAnnotation(AuthRunnerPrincipal.class)).required()) {
            return getRunnerIfExist(authHeader);
        }

        if (Objects.isNull(authHeader)) {
            throw new OauthRequestException(ClientErrorCode.OAUTH_AUTHORIZATION_VALUE_IS_NULL);
        }
        if (!authHeader.startsWith(BEARER)) {
            throw new OauthRequestException(ClientErrorCode.OAUTH_AUTHORIZATION_BEARER_TYPE_NOT_FOUND);
        }

        final String token = authHeader.substring(BEARER.length());
        final Claims claims = jwtDecoder.parseJwtToken(token);
        final String socialId = claims.get("socialId", String.class);
        final Runner foundRunner = oauthRunnerRepository.joinByMemberSocialId(socialId)
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));

        return foundRunner;
    }

    // FIXME: 2023/08/03 예쁘게 고치기
    private Runner getRunnerIfExist(final String authHeader) {
        if (Objects.isNull(authHeader)) {
            return null;
        }
        if (!authHeader.startsWith(BEARER)) {
            throw new OauthRequestException(ClientErrorCode.OAUTH_AUTHORIZATION_BEARER_TYPE_NOT_FOUND);
        }

        final String token = authHeader.substring(BEARER.length());
        final Claims claims = jwtDecoder.parseJwtToken(token);
        final String email = claims.get("email", String.class);
        final Runner foundRunner = oauthRunnerRepository.joinByMemberSocialId(email)
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));

        return foundRunner;
    }
}