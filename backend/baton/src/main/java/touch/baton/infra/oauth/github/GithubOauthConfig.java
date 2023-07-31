package touch.baton.infra.oauth.github;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.github")
public record GithubOauthConfig(String redirectUri,
                                String clientId,
                                String clientSecret,
                                String[] scope
) {
}
