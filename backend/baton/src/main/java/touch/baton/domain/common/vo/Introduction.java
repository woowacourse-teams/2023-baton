package touch.baton.domain.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Introduction {

    private static final String DEFAULT_VALUE = "'안녕하세요.'";

    @ColumnDefault(DEFAULT_VALUE)
    @Column(name = "introduction", nullable = true)
    private String value;

    public Introduction(final String value) {
        this.value = value;
    }

    public static String getDefaultValue() {
        return DEFAULT_VALUE;
    }
}
