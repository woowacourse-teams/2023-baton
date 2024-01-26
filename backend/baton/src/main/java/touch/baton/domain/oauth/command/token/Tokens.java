package touch.baton.domain.oauth.command.token;

public record Tokens(
        AccessToken accessToken,
        RefreshToken2 refreshToken
) {
}
