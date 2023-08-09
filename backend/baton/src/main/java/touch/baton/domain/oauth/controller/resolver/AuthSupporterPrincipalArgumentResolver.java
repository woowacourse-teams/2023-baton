package touch.baton.domain.oauth.controller.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.oauth.exception.OauthRequestException;
import touch.baton.domain.oauth.repository.OauthSupporterRepository;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;
import touch.baton.infra.auth.jwt.JwtDecoder;

import java.util.Collections;

@Component
public class AuthSupporterPrincipalArgumentResolver extends UserPrincipalArgumentResolver {

    private final OauthSupporterRepository oauthSupporterRepository;

    public AuthSupporterPrincipalArgumentResolver(final JwtDecoder jwtDecoder, final OauthSupporterRepository oauthSupporterRepository) {
        super(jwtDecoder);
        this.oauthSupporterRepository = oauthSupporterRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthSupporterPrincipal.class);
    }

    @Override
    protected Object getGuest() {
        return Supporter.builder()
                .reviewCount(new ReviewCount(0))
                .introduction(new Introduction("게스트"))
                .member(null)
                .supporterTechnicalTags(new SupporterTechnicalTags(Collections.emptyList()))
                .build();
    }

    @Override
    protected Object getUser(final String socialId) {
        return oauthSupporterRepository.joinByMemberSocialId(new SocialId(socialId))
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));
    }

    @Override
    protected boolean isAuthorizationRequired(final MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthSupporterPrincipal.class).required();
    }
}
