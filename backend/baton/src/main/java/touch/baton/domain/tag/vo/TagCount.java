package touch.baton.domain.tag.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static lombok.AccessLevel.PROTECTED;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class TagCount {

    private static final String DEFAULT_VALUE = "1";

    @ColumnDefault(DEFAULT_VALUE)
    @Column(name = "tag_count", nullable = false)
    private int value;

    public TagCount(final int value) {
        this.value = value;
    }

    public static TagCount init() {
        return new TagCount(INITIAL_VALUE);
    }

    public TagCount increase() {
        return new TagCount(value + 1);
    }

    public TagCount decrease() {
        return new TagCount(value - 1);
    }
}
