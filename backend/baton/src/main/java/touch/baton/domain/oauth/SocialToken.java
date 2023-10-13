package touch.baton.domain.oauth;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public final class SocialToken {

    private final String value;

    public SocialToken(final String value) {
        this.value = value;
    }
}
