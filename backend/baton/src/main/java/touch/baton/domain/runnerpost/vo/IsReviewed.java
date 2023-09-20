package touch.baton.domain.runnerpost.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class IsReviewed {

    @ColumnDefault(value = "false")
    @Column(name = "is_reviewed", nullable = false)
    private boolean value = false;

    private IsReviewed(final boolean value) {
        this.value = value;
    }

    public static IsReviewed notReviewed() {
        return new IsReviewed(false);
    }

    public static IsReviewed reviewed() {
        return new IsReviewed(true);
    }

    public boolean getValue() {
        return false;
    }
}
