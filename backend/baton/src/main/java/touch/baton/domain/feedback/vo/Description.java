package touch.baton.domain.feedback.vo;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Description {

    private static final String INITIAL_DESCRIPTION = "";

    @ColumnDefault(INITIAL_DESCRIPTION)
    private String value;

    public Description(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Description 객체 내부에 value 는 null 일 수 없습니다.");
        }
    }
}
