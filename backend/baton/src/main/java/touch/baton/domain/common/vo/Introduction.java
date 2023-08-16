package touch.baton.domain.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.exception.DomainException;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Introduction {

    private static final String DEFAULT_VALUE = "안녕하세요.";

    @Column(name = "introduction", nullable = false)
    private String value = DEFAULT_VALUE;

    public Introduction(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Introduction 의 value 는 null 일 수 없습니다.");
        }
    }

    public static Introduction getDefaultIntroduction() {
        return new Introduction(DEFAULT_VALUE);
    }
}
