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
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.oauth.exception.OauthRequestException;
import touch.baton.domain.oauth.repository.OauthSupporterRepository;
import touch.baton.domain.supporter.Supporter;
import touch.baton.infra.auth.jwt.JwtDecoder;

import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class AuthSupporterPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BEARER = "Bearer ";

    private final JwtDecoder jwtDecoder;
    private final OauthSupporterRepository oauthSupporterRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthSupporterPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory
    ) throws Exception {
        final String authHeader = webRequest.getHeader(AUTHORIZATION);
        if (Objects.isNull(authHeader)) {
            return Supporter.builder()
                    .introduction(new Introduction(""))
                    .supporterTechnicalTags(null)
                    .member(null)
                    .build();
        }
        if (!authHeader.startsWith(BEARER)) {
            throw new OauthRequestException(ClientErrorCode.OAUTH_AUTHORIZATION_BEARER_TYPE_NOT_FOUND);
        }

        final String token = authHeader.substring(BEARER.length());
        final Claims claims = jwtDecoder.parseJwtToken(token);
        final String socialId = claims.get("socialId", String.class);
        return oauthSupporterRepository.joinByMemberSocialId(socialId)
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));
    }
}
