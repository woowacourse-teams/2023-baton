package touch.baton.infra.oauth.github.request;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@JsonNaming(SnakeCaseStrategy.class)
public record GithubTokenRequest(String clientId,
                                 String clientSecret,
                                 String code,
                                 String redirectUri
) {
}
