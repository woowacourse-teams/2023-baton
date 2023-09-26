package touch.baton.tobe.domain.oauth.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.infra.auth.jwt.JwtConfig;
import touch.baton.infra.auth.jwt.JwtDecoder;
import touch.baton.infra.auth.jwt.JwtEncoder;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.tobe.domain.oauth.command.AuthorizationHeader;
import touch.baton.tobe.domain.oauth.command.authcode.AuthCodeRequestUrlProviderComposite;
import touch.baton.tobe.domain.oauth.command.client.OauthInformationClientComposite;
import touch.baton.tobe.domain.oauth.command.exception.OauthRequestException;
import touch.baton.tobe.domain.oauth.command.repository.OauthMemberCommandRepository;
import touch.baton.tobe.domain.oauth.command.repository.OauthRunnerCommandRepository;
import touch.baton.tobe.domain.oauth.command.repository.OauthSupporterCommandRepository;
import touch.baton.tobe.domain.oauth.command.repository.RefreshTokenCommandRepository;
import touch.baton.tobe.domain.oauth.command.token.ExpireDate;
import touch.baton.tobe.domain.oauth.command.token.RefreshToken;
import touch.baton.tobe.domain.oauth.command.token.Token;
import touch.baton.tobe.domain.oauth.command.token.Tokens;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static touch.baton.fixture.vo.AuthorizationHeaderFixture.bearerAuthorizationHeader;

@ExtendWith(MockitoExtension.class)
class OauthCommandServiceUpdateTest {

    private OauthCommandService oauthCommandService;
    @Mock
    private AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    @Mock
    private OauthInformationClientComposite oauthInformationClientComposite;
    @Mock
    private OauthMemberCommandRepository oauthMemberCommandRepository;
    @Mock
    private OauthRunnerCommandRepository oauthRunnerCommandRepository;
    @Mock
    private OauthSupporterCommandRepository oauthSupporterCommandRepository;
    @Mock
    private RefreshTokenCommandRepository refreshTokenCommandRepository;
    private JwtEncoder jwtEncoder;
    private JwtEncoder expiredJwtEncoder;
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        final JwtConfig normalJwtConfig = new JwtConfig("secret-key-secret-key-secret-key-secret-key-secret-key-secret-key", "test-issuer", 30);
        jwtDecoder = new JwtDecoder(normalJwtConfig);
        jwtEncoder = new JwtEncoder(normalJwtConfig);

        oauthCommandService = new OauthCommandService(authCodeRequestUrlProviderComposite, oauthInformationClientComposite, oauthMemberCommandRepository, oauthRunnerCommandRepository, oauthSupporterCommandRepository, refreshTokenCommandRepository, jwtEncoder, jwtDecoder);

        final JwtConfig expiredJwtConfig = new JwtConfig("secret-key-secret-key-secret-key-secret-key-secret-key-secret-key", "test-issuer", -1);
        expiredJwtEncoder = new JwtEncoder(expiredJwtConfig);
    }

    @DisplayName("만료된 jwt 와 만료되지 않은 refreshToken 이 주어지면 토큰들이 정상 발급된다.")
    @Test
    void success_reissueAccessToken() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();
        final AuthorizationHeader expiredAuthorizationHeader = bearerAuthorizationHeader(expiredJwtEncoder.jwtToken(Map.of("socialId", tokenOwner.getSocialId().getValue())));
        final String refreshTokenValue = "ethan-refresh-token";
        final RefreshToken beforeRefreshToken = RefreshToken.builder()
                .member(tokenOwner)
                .token(new Token(refreshTokenValue))
                .expireDate(new ExpireDate(LocalDateTime.now().plusDays(30)))
                .build();

        given(oauthMemberCommandRepository.findBySocialId(eq(new SocialId(tokenOwner.getSocialId().getValue())))).willReturn(Optional.of(tokenOwner));
        given(refreshTokenCommandRepository.findByToken(eq(new Token(refreshTokenValue)))).willReturn(Optional.of(beforeRefreshToken));

        // when
        final Tokens tokens = oauthCommandService.reissueAccessToken(expiredAuthorizationHeader, refreshTokenValue);

        // then
        assertSoftly(softly -> {
            softly.assertThat(tokens.accessToken()).isNotNull();
            softly.assertThat(tokens.accessToken().getValue()).isNotEqualTo(expiredAuthorizationHeader.parseBearerAccessToken());
            softly.assertThat(tokens.refreshToken()).isNotNull();
            softly.assertThat(tokens.refreshToken().getToken().getValue()).isNotEqualTo(refreshTokenValue);
        });
    }

    @DisplayName("만료되지 않은 jwt 로 재발급 요청하면 오류가 발생한다.")
    @Test
    void fail_reissueAccessToken_when_jwt_is_not_expired() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();
        final AuthorizationHeader normalJwtToken = bearerAuthorizationHeader(jwtEncoder.jwtToken(Map.of("socialId", tokenOwner.getSocialId().getValue())));
        final String refreshTokenValue = "ethan-refresh-token";
        final RefreshToken beforeRefreshToken = RefreshToken.builder()
                .member(tokenOwner)
                .token(new Token(refreshTokenValue))
                .expireDate(new ExpireDate(LocalDateTime.now().plusDays(30)))
                .build();

        // when, then
        assertThatThrownBy(() -> oauthCommandService.reissueAccessToken(normalJwtToken, refreshTokenValue)).isInstanceOf(OauthRequestException.class);
    }

    @DisplayName("없는 socialId 가 재발급 요청하면 오류가 발생한다.")
    @Test
    void fail_reissueAccessToken_when_socialId_not_exists() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();
        final AuthorizationHeader expiredAuthorizationHeader = bearerAuthorizationHeader(expiredJwtEncoder.jwtToken(Map.of("socialId", tokenOwner.getSocialId().getValue())));
        final String refreshTokenValue = "ethan-refresh-token";

        given(oauthMemberCommandRepository.findBySocialId(eq(new SocialId(tokenOwner.getSocialId().getValue())))).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> oauthCommandService.reissueAccessToken(expiredAuthorizationHeader, refreshTokenValue)).isInstanceOf(OauthRequestException.class);
    }

    @DisplayName("refreshToken 이 없으면 재발급 요청하면 오류가 발생한다.")
    @Test
    void fail_reissueAccessToken_when_refreshToken_not_exists() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();
        final AuthorizationHeader expiredAuthorizationHeader = bearerAuthorizationHeader(expiredJwtEncoder.jwtToken(Map.of("socialId", tokenOwner.getSocialId().getValue())));
        final String refreshTokenValue = "ethan-refresh-token";

        given(oauthMemberCommandRepository.findBySocialId(eq(new SocialId(tokenOwner.getSocialId().getValue())))).willReturn(Optional.of(tokenOwner));
        given(refreshTokenCommandRepository.findByToken(eq(new Token(refreshTokenValue)))).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> oauthCommandService.reissueAccessToken(expiredAuthorizationHeader, refreshTokenValue)).isInstanceOf(OauthRequestException.class);
    }

    @DisplayName("주인이 아닌 accessToken 으로 재발급 요청하면 오류가 발생한다.")
    @Test
    void fail_reissueAccessToken_when_not_owner_of_accessToken() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();
        final AuthorizationHeader expiredAuthorizationHeader = bearerAuthorizationHeader(expiredJwtEncoder.jwtToken(Map.of("socialId", tokenOwner.getSocialId().getValue())));
        final String refreshTokenValue = "ethan-refresh-token";
        final RefreshToken beforeRefreshToken = RefreshToken.builder()
                .member(tokenOwner)
                .token(new Token(refreshTokenValue))
                .expireDate(new ExpireDate(LocalDateTime.now().plusDays(30)))
                .build();

        final Member notTokenOwner = MemberFixture.createHyena();
        given(oauthMemberCommandRepository.findBySocialId(eq(new SocialId(tokenOwner.getSocialId().getValue())))).willReturn(Optional.of(notTokenOwner));
        given(refreshTokenCommandRepository.findByToken(eq(new Token(refreshTokenValue)))).willReturn(Optional.of(beforeRefreshToken));

        // when, then
        assertThatThrownBy(() -> oauthCommandService.reissueAccessToken(expiredAuthorizationHeader, refreshTokenValue)).isInstanceOf(OauthRequestException.class);
    }

    @DisplayName("만료된 refreshToken 으로  재발급 요청하면 오류가 발생한다.")
    @Test
    void fail_reissueAccessToken_when_refreshToken_is_expired() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();
        final AuthorizationHeader expiredAuthorizationHeader = bearerAuthorizationHeader(expiredJwtEncoder.jwtToken(Map.of("socialId", tokenOwner.getSocialId().getValue())));
        final String refreshTokenValue = "ethan-refresh-token";
        final RefreshToken beforeRefreshToken = RefreshToken.builder()
                .member(tokenOwner)
                .token(new Token(refreshTokenValue))
                .expireDate(new ExpireDate(LocalDateTime.now().minusDays(14)))
                .build();

        given(oauthMemberCommandRepository.findBySocialId(eq(new SocialId(tokenOwner.getSocialId().getValue())))).willReturn(Optional.of(tokenOwner));
        given(refreshTokenCommandRepository.findByToken(eq(new Token(refreshTokenValue)))).willReturn(Optional.of(beforeRefreshToken));

        // when, then
        assertThatThrownBy(() -> oauthCommandService.reissueAccessToken(expiredAuthorizationHeader, refreshTokenValue)).isInstanceOf(OauthRequestException.class);
    }
}
