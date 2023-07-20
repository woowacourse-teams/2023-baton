package touch.baton.domain.tag.vo;

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
public class TagCount {

    private static final String DEFAULT_VALUE = "1";

    @ColumnDefault(DEFAULT_VALUE)
    @Column(name = "tag_count", nullable = false)
    private int value;

    public TagCount(final int value) {
        this.value = value;
    }

    public static TagCount initial() {
        return new TagCount(Integer.parseInt(DEFAULT_VALUE));
    }

    public TagCount increase() {
        return new TagCount(value + 1);
    }

    public TagCount decrease() {
        return new TagCount(value - 1);
    }
}
