package touch.baton.config.infra.auth.oauth;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.infra.auth.jwt.JwtConfig;
import touch.baton.infra.auth.jwt.JwtEncoder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class MockRefreshTokenConfig {

    @Bean
    JwtEncoder expiredJwtDecoder() {
        return new JwtEncoder(mockJwtConfig());
    }

    private JwtConfig mockJwtConfig() {
        return new JwtConfig("test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key", "test_issuer", -1);
    }

    @Bean
    RefreshToken refreshToken() {
        final RefreshToken mock = mock(RefreshToken.class);

        /**
         * 다야한 when 절
         */
        when(mock.getToken().getValue()).thenReturn("mock refresh token");

        return mock;
    }
}
