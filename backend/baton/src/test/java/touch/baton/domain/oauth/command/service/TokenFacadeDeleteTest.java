package touch.baton.domain.oauth.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.repository.RefreshTokenCommandRepository;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.infra.auth.jwt.JwtConfig;
import touch.baton.infra.auth.jwt.JwtEncoder;

import static org.mockito.BDDMockito.only;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenFacadeDeleteTest {

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
    void success() {
        // given
        final Member tokenOwner = MemberFixture.createEthan();

        // when
        tokenFacade.logout(tokenOwner.getSocialId());

        // then
        verify(refreshTokenCommandRepository, only()).deleteById(tokenOwner.getSocialId().getValue());
    }
}
