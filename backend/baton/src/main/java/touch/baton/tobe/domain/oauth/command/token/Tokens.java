package touch.baton.tobe.domain.oauth.command.token;

public record Tokens(
        AccessToken accessToken,
        RefreshToken refreshToken
) {
}
