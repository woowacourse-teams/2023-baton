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

    @Column(name = "introduction", nullable = true)
    @ColumnDefault("안녕하세요.")
    private String value;

    public Introduction(final String value) {
        this.value = value;
    }

    public void updateIntroduction(String value) {
        this.value = defaultIntroductionIfNull(value);
    }

    private String defaultIntroductionIfNull(final String value) {
        if (Objects.isNull(value)) {
            return this.value;
        }
        return value;
    }
}
