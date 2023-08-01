package touch.baton.domain.oauth.controller;

import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import touch.baton.domain.oauth.repository.OauthSupporterRepository;
import touch.baton.domain.supporter.Supporter;
import touch.baton.infra.auth.jwt.JwtDecoder;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class AuthSupporterPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

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
        final Map<String, Claim> claims = jwtDecoder.parseJwtToken(authHeader);
        final String email = claims.get("email").asString();
        final Supporter foundSupporter = oauthSupporterRepository.joinByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("여기도 고쳐"));

        return foundSupporter;
    }
}
