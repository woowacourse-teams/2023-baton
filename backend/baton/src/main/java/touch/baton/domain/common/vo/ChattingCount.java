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
public class ChattingCount {

    private static final String DEFAULT_VALUE = "0";

    @ColumnDefault(DEFAULT_VALUE)
    @Column(name = "chatting_room_count", nullable = false)
    private int value;

    public ChattingCount(final int value) {
        this.value = value;
    }

    public static ChattingCount zero() {
        return new ChattingCount(Integer.parseInt(DEFAULT_VALUE));
    }
}
