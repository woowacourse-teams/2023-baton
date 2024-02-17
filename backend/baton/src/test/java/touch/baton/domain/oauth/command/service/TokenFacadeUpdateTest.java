package touch.baton.domain.oauth.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.exception.OauthRequestException;
import touch.baton.domain.oauth.command.repository.RefreshTokenCommandRepository;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RefreshTokenFixture;
import touch.baton.infra.auth.jwt.JwtConfig;
import touch.baton.infra.auth.jwt.JwtEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenFacadeUpdateTest {

    private TokenFacade tokenFacade;

    @Mock
    private RefreshTokenCommandRepository refreshTokenCommandRepository;

    @Mock
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        final JwtConfig normalJwtConfig = new JwtConfig("secret-key-secret-key-secret-key-secret-key-secret-key-secret-key", "test-issuer", 30);
        jwtEncoder = new JwtEncoder(normalJwtConfig);
        tokenFacade = new TokenFacade(refreshTokenCommandRepository, jwtEncoder, 30L);
    }

    @DisplayName("만료되지 않은 Refreshtoken을 가지고 재발급을 요청하면 성공한다.")
    @Test
    void success_reissueAccessToken() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();
        final String refreshTokenValue = "ethan-refresh-token";

        final RefreshToken ownerRefreshToken = RefreshTokenFixture.ethanRefreshToken();
        given(refreshTokenCommandRepository.findById(tokenOwner.getSocialId().getValue())).willReturn(Optional.of(ownerRefreshToken));

        // when
        // then
        assertThatCode(() -> tokenFacade.reissueAccessToken(tokenOwner, new Token(refreshTokenValue))).doesNotThrowAnyException();
    }

    @DisplayName("주인이 아닌 accessToken 으로 재발급 요청하면 오류가 발생한다.")
    @Test
    void fail_reissueAccessToken_when_not_owner_of_accessToken() {
        // given
        final Member notTokenOwner = MemberFixture.createHyena();
        final RefreshToken notOwnerRefreshToken = RefreshTokenFixture.hyenaRefreshToken();

        given(refreshTokenCommandRepository.findById(eq(notTokenOwner.getSocialId().getValue()))).willReturn(Optional.of(notOwnerRefreshToken));

        // when, then
        final String ownerRefreshTokenValue = "ethan-refresh-token";
        assertThatThrownBy(() -> tokenFacade.reissueAccessToken(notTokenOwner, new Token(ownerRefreshTokenValue))).isInstanceOf(OauthRequestException.class);
    }

    @DisplayName("만료된 refreshToken 으로 재발급 요청하면 오류가 발생한다.")
    @Test
    void fail_reissueAccessToken_when_refreshToken_is_expired() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();
        final String refreshTokenValue = "ethan-refresh-token";

        given(refreshTokenCommandRepository.findById(eq(tokenOwner.getSocialId().getValue()))).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tokenFacade.reissueAccessToken(tokenOwner, new Token(refreshTokenValue))).isInstanceOf(OauthRequestException.class);
    }

    @DisplayName("createTokens으로 AccessToken과 RefreshToken을 생성한다.")
    @Test
    void success_createTokens() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();

        // when
        tokenFacade.createTokens(tokenOwner);

        // then
        verify(refreshTokenCommandRepository).save(any(RefreshToken.class));
    }
}
