package touch.baton.domain.oauth.query.controller.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.vo.Company;
import touch.baton.domain.member.command.vo.GithubUrl;
import touch.baton.domain.member.command.vo.ImageUrl;
import touch.baton.domain.member.command.vo.MemberName;
import touch.baton.domain.member.command.vo.OauthId;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.command.exception.OauthRequestException;
import touch.baton.domain.oauth.command.repository.OauthRunnerCommandRepository;
import touch.baton.infra.auth.jwt.JwtDecoder;

@Component
public class AuthRunnerPrincipalArgumentResolver extends UserPrincipalArgumentResolver {

    private final OauthRunnerCommandRepository oauthRunnerCommandRepository;

    public AuthRunnerPrincipalArgumentResolver(final JwtDecoder jwtDecoder, final OauthRunnerCommandRepository oauthRunnerCommandRepository) {
        super(jwtDecoder);
        this.oauthRunnerCommandRepository = oauthRunnerCommandRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthRunnerPrincipal.class);
    }

    @Override
    protected Object getGuest() {
        return Runner.builder()
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
        return oauthRunnerCommandRepository.joinByMemberSocialId(new SocialId(socialId))
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));
    }

    @Override
    protected boolean isAuthorizationRequired(final MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthRunnerPrincipal.class).required();
    }
}
