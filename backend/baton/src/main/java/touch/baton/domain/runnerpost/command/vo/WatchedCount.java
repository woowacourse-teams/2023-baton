package touch.baton.domain.runnerpost.command.vo;

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
public class WatchedCount {

    private static final String DEFAULT_VALUE = "0";

    @ColumnDefault(DEFAULT_VALUE)
    @Column(name = "watched_count", nullable = false)
    private int value;

    public WatchedCount(final int value) {
        this.value = value;
    }

    public static WatchedCount zero() {
        return new WatchedCount(Integer.parseInt(DEFAULT_VALUE));
    }

    public WatchedCount increase() {
        return new WatchedCount(value + 1);
    }
}
