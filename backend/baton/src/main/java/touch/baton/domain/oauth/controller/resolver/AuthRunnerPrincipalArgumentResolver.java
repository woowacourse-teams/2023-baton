package touch.baton.domain.oauth.controller.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.oauth.exception.OauthRequestException;
import touch.baton.domain.oauth.repository.OauthRunnerRepository;
import touch.baton.domain.runner.Runner;
import touch.baton.infra.auth.jwt.JwtDecoder;

@Component
public class AuthRunnerPrincipalArgumentResolver extends UserPrincipalArgumentResolver {

    private final OauthRunnerRepository oauthRunnerRepository;

    public AuthRunnerPrincipalArgumentResolver(final JwtDecoder jwtDecoder, final OauthRunnerRepository oauthRunnerRepository) {
        super(jwtDecoder);
        this.oauthRunnerRepository = oauthRunnerRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthRunnerPrincipal.class);
    }

    @Override
    protected Object getGuest() {
        return Runner.builder()
                .introduction(new Introduction("게스트"))
                .member(Member.builder()
                        .memberName(new MemberName("게스트"))
                        .socialId(new SocialId("guestSocialId"))
                        .oauthId(new OauthId("guestOauthId"))
                        .githubUrl(new GithubUrl("guestGithubUrl"))
                        .company(new Company("guestCompany"))
                        .imageUrl(new ImageUrl("guestImageUrl"))
                        .build()
                );
    }

    @Override
    protected Object getUser(final String socialId) {
        return oauthRunnerRepository.joinByMemberSocialId(new SocialId(socialId))
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));
    }

    @Override
    protected boolean isAuthorizationRequired(final MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthRunnerPrincipal.class).required();
    }
}
