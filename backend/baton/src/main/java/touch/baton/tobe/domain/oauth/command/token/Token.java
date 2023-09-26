package touch.baton.tobe.domain.oauth.command.token;

import jakarta.persistence.Column;
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

    @Column(name = "token", nullable = false, columnDefinition = "text")
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
