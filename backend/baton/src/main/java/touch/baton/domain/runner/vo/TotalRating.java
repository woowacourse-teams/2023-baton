package touch.baton.domain.runner.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class TotalRating {

    @Column(name = "total_rating")
    private Integer value;

    public TotalRating(final Integer value) {
        this.value = value;
    }
}
