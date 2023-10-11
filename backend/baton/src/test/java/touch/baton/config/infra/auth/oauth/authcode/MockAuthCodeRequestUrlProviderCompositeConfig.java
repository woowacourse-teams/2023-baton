package touch.baton.config.infra.auth.oauth.authcode;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import touch.baton.domain.oauth.command.OauthType;
import touch.baton.domain.oauth.command.authcode.AuthCodeRequestUrlProviderComposite;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;

@TestConfiguration
public abstract class MockAuthCodeRequestUrlProviderCompositeConfig {

    @Bean
    AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite() {
        final AuthCodeRequestUrlProviderComposite mock = Mockito.mock(AuthCodeRequestUrlProviderComposite.class);
        when(mock.findRequestUrl(any(OauthType.class))).thenReturn("https://redirect-test.com");

        return mock;
    }
}
