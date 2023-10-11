package touch.baton.domain.oauth.command;

public class AuthorizationHeader {

    private static final String BEARER = "Bearer ";

    private final String value;

    public AuthorizationHeader(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (value == null) {
            throw new IllegalArgumentException("AuthorizationHeader 의 value 는 null 일 수 없습니다.");
        }
    }

    public String parseBearerAccessToken() {
        return value.substring(BEARER.length());
    }

    public boolean isNotBearerAuth() {
        return !value.startsWith(BEARER);
    }
}
