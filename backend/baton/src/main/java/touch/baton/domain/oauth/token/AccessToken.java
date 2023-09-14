package touch.baton.domain.oauth.token;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class AccessToken {

    private final String value;

    public AccessToken(final String value) {
        this.value = value;
    }
}
