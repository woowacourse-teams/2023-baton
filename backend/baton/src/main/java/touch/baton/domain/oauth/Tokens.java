package touch.baton.domain.oauth;

public record Tokens(
        AccessToken accessToken,
        RefreshToken refreshToken
) {
}
