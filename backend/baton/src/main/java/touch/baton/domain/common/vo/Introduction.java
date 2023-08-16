package touch.baton.domain.common.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

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
        this.value = value;
    }

    public static Introduction getDefaultIntroduction() {
        return new Introduction(DEFAULT_VALUE);
    }
}
