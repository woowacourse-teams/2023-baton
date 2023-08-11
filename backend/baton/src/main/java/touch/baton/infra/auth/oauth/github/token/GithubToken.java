package touch.baton.infra.auth.oauth.github.token;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@JsonNaming(SnakeCaseStrategy.class)
public record GithubToken(String tokenType,
                          String accessToken
) {
}
