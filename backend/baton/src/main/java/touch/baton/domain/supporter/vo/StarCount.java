package touch.baton.domain.supporter.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class StarCount {

    private static final String DEFAULT_VALUE = "0";

    @ColumnDefault(DEFAULT_VALUE)
    @Column(name = "star_count", nullable = false)
    private int value;

    public StarCount(final int value) {
        this.value = value;
    }
}
