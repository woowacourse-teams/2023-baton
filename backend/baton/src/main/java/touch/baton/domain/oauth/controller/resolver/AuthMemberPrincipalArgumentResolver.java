package touch.baton.domain.oauth.controller.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.vo.Company;
import touch.baton.tobe.domain.member.command.vo.GithubUrl;
import touch.baton.tobe.domain.member.command.vo.ImageUrl;
import touch.baton.tobe.domain.member.command.vo.MemberName;
import touch.baton.tobe.domain.member.command.vo.OauthId;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.exception.OauthRequestException;
import touch.baton.domain.oauth.repository.OauthMemberRepository;
import touch.baton.infra.auth.jwt.JwtDecoder;

@Component
public class AuthMemberPrincipalArgumentResolver extends UserPrincipalArgumentResolver {

    private final OauthMemberRepository oauthMemberRepository;

    public AuthMemberPrincipalArgumentResolver(final JwtDecoder jwtDecoder, final OauthMemberRepository oauthMemberRepository) {
        super(jwtDecoder);
        this.oauthMemberRepository = oauthMemberRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMemberPrincipal.class);
    }

    @Override
    protected Object getGuest() {
        return Member.builder()
                .memberName(new MemberName("게스트"))
                .socialId(new SocialId("게스트 SocialId"))
                .oauthId(new OauthId("게스트 OauthId"))
                .githubUrl(new GithubUrl("게스트 GitHubUrl"))
                .company(new Company("게스트 회사"))
                .imageUrl(new ImageUrl("게스트 이미지"))
                .build();
    }

    @Override
    protected Object getUser(final String socialId) {
        return oauthMemberRepository.findBySocialId(new SocialId(socialId))
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));
    }

    @Override
    protected boolean isAuthorizationRequired(final MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthMemberPrincipal.class).required();
    }
}
