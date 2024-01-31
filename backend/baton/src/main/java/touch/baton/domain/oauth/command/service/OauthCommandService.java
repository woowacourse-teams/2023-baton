package touch.baton.domain.oauth.command.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.Company;
import touch.baton.domain.member.command.vo.ReviewCount;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.command.AuthorizationHeader;
import touch.baton.domain.oauth.command.OauthInformation;
import touch.baton.domain.oauth.command.OauthType;
import touch.baton.domain.oauth.command.authcode.AuthCodeRequestUrlProviderComposite;
import touch.baton.domain.oauth.command.client.OauthInformationClientComposite;
import touch.baton.domain.oauth.command.exception.OauthRequestException;
import touch.baton.domain.oauth.command.repository.OauthMemberCommandRepository;
import touch.baton.domain.oauth.command.repository.OauthRunnerCommandRepository;
import touch.baton.domain.oauth.command.repository.OauthSupporterCommandRepository;
import touch.baton.domain.oauth.command.repository.RefreshTokenCommandRepository2;
import touch.baton.domain.oauth.command.token.AccessToken;
import touch.baton.domain.oauth.command.token.RefreshToken2;
import touch.baton.domain.oauth.command.token.Token2;
import touch.baton.domain.oauth.command.token.Tokens;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTags;
import touch.baton.infra.auth.jwt.JwtDecoder;
import touch.baton.infra.auth.jwt.JwtEncoder;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class OauthCommandService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthInformationClientComposite oauthInformationClientComposite;
    private final OauthMemberCommandRepository oauthMemberCommandRepository;
    private final OauthRunnerCommandRepository oauthRunnerCommandRepository;
    private final OauthSupporterCommandRepository oauthSupporterCommandRepository;
    private final RefreshTokenCommandRepository2 refreshTokenCommandRepository2;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${refresh_token.expire_minutes}")
    private Long refreshTokenExpireMinutes;

    public String readAuthCodeRedirect(final OauthType oauthType) {
        return authCodeRequestUrlProviderComposite.findRequestUrl(oauthType);
    }

    public Tokens login(final OauthType oauthType, final String code) {
        final OauthInformation oauthInformation = oauthInformationClientComposite.fetchInformation(oauthType, code);

        final Optional<Member> maybeMember = oauthMemberCommandRepository.findMemberByOauthId(oauthInformation.getOauthId());
        if (maybeMember.isEmpty()) {
            final Member savedMember = signUpMember(oauthInformation);
            saveNewRunner(savedMember);
            saveNewSupporter(savedMember);
            return createTokens(savedMember);
        }

        return createTokens(maybeMember.get());
    }

    private Member signUpMember(final OauthInformation oauthInformation) {
        final Member newMember = Member.builder()
                .memberName(oauthInformation.getMemberName())
                .socialId(oauthInformation.getSocialId())
                .oauthId(oauthInformation.getOauthId())
                .githubUrl(oauthInformation.getGithubUrl())
                .company(new Company(""))
                .imageUrl(oauthInformation.getImageUrl())
                .build();

        return oauthMemberCommandRepository.save(newMember);
    }

    private void saveNewRunner(final Member member) {
        final Runner newRunner = Runner.builder()
                .member(member)
                .build();

        oauthRunnerCommandRepository.save(newRunner);
    }

    private void saveNewSupporter(final Member member) {
        final Supporter newSupporter = Supporter.builder()
                .reviewCount(new ReviewCount(0))
                .member(member)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();

        oauthSupporterCommandRepository.save(newSupporter);
    }

    public Tokens reissueAccessToken(final AuthorizationHeader authHeader, final Token2 refreshToken) {
        final Claims claims = jwtDecoder.parseExpiredAuthorizationHeader(authHeader);
        final SocialId socialId = new SocialId(claims.get("socialId", String.class));
        final Member findMember = oauthMemberCommandRepository.findBySocialId(socialId)
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));

        final RefreshToken2 findRefreshToken = refreshTokenCommandRepository2.findById(findMember.getSocialId().getValue())
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.REFRESH_TOKEN_IS_NOT_FOUND));
        if (findRefreshToken.isNotOwner(refreshToken)) {
            throw new OauthRequestException(ClientErrorCode.ACCESS_TOKEN_AND_REFRESH_TOKEN_HAVE_DIFFERENT_OWNER);
        }

        return createTokens(findMember);
    }

    private Tokens createTokens(final Member member) {
        final AccessToken accessToken = createAccessToken(member.getSocialId());
        final RefreshToken2 refreshToken2 = createRefreshToken(member);

        return new Tokens(accessToken, refreshToken2);
    }

    private AccessToken createAccessToken(final SocialId socialId) {
        final String jwtToken = jwtEncoder.jwtToken(
                Map.of("socialId", socialId.getValue()));
        return new AccessToken(jwtToken);
    }

    private RefreshToken2 createRefreshToken(final Member member) {
        final Token2 token = new Token2(UUID.randomUUID().toString());
        final RefreshToken2 refreshToken2 = RefreshToken2.builder()
                .socialId(member.getSocialId().getValue())
                .member(member)
                .token(token)
                .timeout(refreshTokenExpireMinutes)
                .build();

        refreshTokenCommandRepository2.save(refreshToken2);

        return refreshToken2;
    }

    public void logout(final Member member) {
        refreshTokenCommandRepository2.deleteById(member.getSocialId().getValue());
    }
}
