package touch.baton.domain.oauth.token;

public record Tokens(
        AccessToken accessToken,
        RefreshToken refreshToken
) {
}
