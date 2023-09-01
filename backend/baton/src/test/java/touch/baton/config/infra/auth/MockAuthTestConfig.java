package touch.baton.config.infra.auth;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import touch.baton.config.infra.auth.jwt.MockJwtConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodeRequestUrlProviderCompositeConfig;
import touch.baton.config.infra.auth.oauth.client.MockOauthInformationClientCompositeConfig;

@Profile("test")
@Import({MockJwtConfig.class,
        MockAuthCodeRequestUrlProviderCompositeConfig.class,
        MockOauthInformationClientCompositeConfig.class})
@TestConfiguration
public abstract class MockAuthTestConfig {
}
