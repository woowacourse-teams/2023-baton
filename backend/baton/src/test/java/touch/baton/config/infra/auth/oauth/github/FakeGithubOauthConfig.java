package touch.baton.config.infra.auth.oauth.github;

import org.assertj.core.util.Arrays;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import touch.baton.infra.auth.oauth.github.GithubOauthConfig;

@TestConfiguration
public abstract class FakeGithubOauthConfig {

    @Bean
    GithubOauthConfig githubOauthConfig() {
        return new GithubOauthConfig(
                "https://test-redirect-uri.com",
                "test_client_id",
                "test_client_secret",
                Arrays.array("test_scope")
        );
    }
}
