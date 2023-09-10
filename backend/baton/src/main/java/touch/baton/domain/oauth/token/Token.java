package touch.baton.domain.oauth.token;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Token {

    private String value;

    public Token(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (ObjectUtils.isEmpty(value)) {
            throw new IllegalArgumentException("RefreshToken 의 value 는 null이거나 비어있을 수 없습니다.");
        }
    }
}
