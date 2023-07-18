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
public class ReviewCount {

    private static final String DEFAULT_VALUE = "0";

    @ColumnDefault(DEFAULT_VALUE)
    @Column(name = "review_count")
    private int value;

    public ReviewCount(final int value) {
        this.value = value;
    }
}
