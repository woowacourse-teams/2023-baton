package touch.baton.config.infra.auth;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import touch.baton.config.infra.auth.jwt.FakeJwtConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodeRequestUrlProviderCompositeConfig;
import touch.baton.config.infra.auth.oauth.client.MockAuthInformationClientCompositeConfig;

@Profile("test")
@Import({FakeJwtConfig.class,
        MockAuthCodeRequestUrlProviderCompositeConfig.class,
        MockAuthInformationClientCompositeConfig.class})
@TestConfiguration
public abstract class MockBeanAuthTestConfig {
}
