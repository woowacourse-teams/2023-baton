package touch.baton.domain.oauth.command;

import static java.util.Locale.ENGLISH;

public enum OauthType {

    GITHUB;

    public static OauthType from(final String name) {
        return OauthType.valueOf(name.toUpperCase(ENGLISH));
    }
}
