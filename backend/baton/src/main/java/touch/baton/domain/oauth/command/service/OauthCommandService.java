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
import touch.baton.domain.oauth.command.repository.RefreshTokenCommandRepository;
import touch.baton.domain.oauth.command.token.AccessToken;
import touch.baton.domain.oauth.command.token.ExpireDate;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;
import touch.baton.domain.oauth.command.token.Tokens;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTags;
import touch.baton.infra.auth.jwt.JwtDecoder;
import touch.baton.infra.auth.jwt.JwtEncoder;

import java.time.LocalDateTime;
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
    private final RefreshTokenCommandRepository refreshTokenCommandRepository;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${refresh_token.expire_minutes}")
    private int refreshTokenExpireMinutes;

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

        return oauthMemberCommandRepository.save(newMember);
    }

    private Runner saveNewRunner(final Member member) {
        final Runner newRunner = Runner.builder()
                .member(member)
                .build();

        return oauthRunnerCommandRepository.save(newRunner);
    }

    private Supporter saveNewSupporter(final Member member) {
        final Supporter newSupporter = Supporter.builder()
                .reviewCount(new ReviewCount(0))
                .member(member)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();

        return oauthSupporterCommandRepository.save(newSupporter);
    }

    private Tokens createTokens(final SocialId socialId, final Member member) {
        final AccessToken accessToken = createAccessToken(socialId);

        final String randomTokens = UUID.randomUUID().toString();
        final Token token = new Token(randomTokens);
        final LocalDateTime expireDate = LocalDateTime.now().plusMinutes(refreshTokenExpireMinutes);
        final RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .token(token)
                .expireDate(new ExpireDate(expireDate))
                .build();

        final Optional<RefreshToken> maybeRefreshToken = refreshTokenCommandRepository.findByMember(member);
        if (maybeRefreshToken.isPresent()) {
            final RefreshToken findRefreshToken = maybeRefreshToken.get();
            findRefreshToken.updateToken(new Token(randomTokens), refreshTokenExpireMinutes);
            return new Tokens(accessToken, findRefreshToken);
        }

        refreshTokenCommandRepository.save(refreshToken);
        return new Tokens(accessToken, refreshToken);
    }

    public Tokens reissueAccessToken(final AuthorizationHeader authHeader, final String refreshToken) {
        final Claims claims = jwtDecoder.parseExpiredAuthorizationHeader(authHeader);
        final SocialId socialId = new SocialId(claims.get("socialId", String.class));
        final Member findMember = oauthMemberCommandRepository.findBySocialId(socialId)
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));

        final RefreshToken findRefreshToken = refreshTokenCommandRepository.findByToken(new Token(refreshToken))
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

        refreshToken.updateToken(new Token(UUID.randomUUID().toString()), refreshTokenExpireMinutes);

        return new Tokens(accessToken, refreshToken);
    }

    private AccessToken createAccessToken(final SocialId socialId) {
        final String jwtToken = jwtEncoder.jwtToken(Map.of(
                "socialId", socialId.getValue())
        );
        return new AccessToken(jwtToken);
    }

    public void logout(final Member member) {
        refreshTokenCommandRepository.deleteByMember(member);
    }
}
