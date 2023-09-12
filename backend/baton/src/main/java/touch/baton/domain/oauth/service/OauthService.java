package touch.baton.domain.oauth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.oauth.OauthInformation;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import touch.baton.domain.oauth.client.OauthInformationClientComposite;
import touch.baton.domain.oauth.exception.OauthRequestException;
import touch.baton.domain.oauth.repository.OauthMemberRepository;
import touch.baton.domain.oauth.repository.OauthRunnerRepository;
import touch.baton.domain.oauth.repository.OauthSupporterRepository;
import touch.baton.domain.oauth.repository.RefreshTokenRepository;
import touch.baton.domain.oauth.token.AccessToken;
import touch.baton.domain.oauth.token.ExpireDate;
import touch.baton.domain.oauth.token.RefreshToken;
import touch.baton.domain.oauth.token.Token;
import touch.baton.domain.oauth.token.Tokens;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;
import touch.baton.infra.auth.jwt.JwtDecoder;
import touch.baton.infra.auth.jwt.JwtEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static touch.baton.domain.oauth.token.RefreshToken.REFRESH_TOKEN_LIFECYCLE;

@RequiredArgsConstructor
@Service
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthInformationClientComposite oauthInformationClientComposite;
    private final OauthMemberRepository oauthMemberRepository;
    private final OauthRunnerRepository oauthRunnerRepository;
    private final OauthSupporterRepository oauthSupporterRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String readAuthCodeRedirect(final OauthType oauthType) {
        return authCodeRequestUrlProviderComposite.findRequestUrl(oauthType);
    }

    public Tokens login(final OauthType oauthType, final String code) {
        final OauthInformation oauthInformation = oauthInformationClientComposite.fetchInformation(oauthType, code);

        final Optional<Member> maybeMember = oauthMemberRepository.findMemberByOauthId(oauthInformation.getOauthId());
        if (maybeMember.isEmpty()) {
            final Member savedMember = signUpMember(oauthInformation);
            saveNewRunner(savedMember);
            saveNewSupporter(savedMember);
            return createTokens(oauthInformation.getSocialId(), savedMember);
        }

        return createTokens(oauthInformation.getSocialId(), maybeMember.get());
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

        return oauthMemberRepository.save(newMember);
    }

    private Runner saveNewRunner(final Member member) {
        final Runner newRunner = Runner.builder()
                .member(member)
                .build();

        return oauthRunnerRepository.save(newRunner);
    }

    private Supporter saveNewSupporter(final Member member) {
        final Supporter newSupporter = Supporter.builder()
                .reviewCount(new ReviewCount(0))
                .member(member)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();

        return oauthSupporterRepository.save(newSupporter);
    }

    private Tokens createTokens(final SocialId socialId, final Member member) {
        final AccessToken accessToken = createAccessToken(socialId);

        final String randomTokens = UUID.randomUUID().toString();
        final Token token = new Token(randomTokens);
        final LocalDateTime expireDate = LocalDateTime.now().plusDays(REFRESH_TOKEN_LIFECYCLE);
        final RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .token(token)
                .expireDate(new ExpireDate(expireDate))
                .build();

        refreshTokenRepository.save(refreshToken);

        return new Tokens(accessToken, refreshToken);
    }

    public Tokens reissueAccessToken(final String jwtToken, final String refreshToken) {
        final Claims claims = jwtDecoder.parseExpiredJwtToken(jwtToken);
        final SocialId socialId = new SocialId(claims.get("socialId", String.class));
        final Member findMember = oauthMemberRepository.findBySocialId(socialId)
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));

        final RefreshToken findRefreshToken = refreshTokenRepository.findByToken(new Token(refreshToken))
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.REFRESH_TOKEN_IS_NOT_FOUND));

        if (findRefreshToken.isNotOwner(findMember)) {
            throw new OauthRequestException(ClientErrorCode.ACCESS_TOKEN_AND_REFRESH_TOKEN_HAVE_DIFFERENT_OWNER);
        }
        if (findRefreshToken.isExpired()) {
            throw new OauthRequestException(ClientErrorCode.REFRESH_TOKEN_IS_ALREADY_EXPIRED);
        }

        return reissueTokens(socialId, findRefreshToken);
    }

    private Tokens reissueTokens(final SocialId socialId, final RefreshToken refreshToken) {
        final AccessToken accessToken = createAccessToken(socialId);

        refreshToken.updateToken(new Token(UUID.randomUUID().toString()));

        return new Tokens(accessToken, refreshToken);
    }

    private AccessToken createAccessToken(final SocialId socialId) {
        final String jwtToken = jwtEncoder.jwtToken(Map.of(
                "socialId", socialId.getValue())
        );
        return new AccessToken(jwtToken);
    }
}
