package touch.baton.domain.oauth.command.token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

@Getter
@EqualsAndHashCode
public class Token2 {

    private String value;

    public Token2(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException("RefreshToken 의 value 는 null이거나 비어있을 수 없습니다.");
        }
    }
}
