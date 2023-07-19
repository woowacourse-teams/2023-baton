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
public class ChattingRoomCount {

    private static final String DEFAULT_VALUE = "0";
    private static final ChattingRoomCount ZERO = new ChattingRoomCount(Integer.parseInt(DEFAULT_VALUE));

    @ColumnDefault(DEFAULT_VALUE)
    @Column(name = "chatting_room_count", nullable = false)
    private int value;

    public ChattingRoomCount(final int value) {
        this.value = value;
    }

    public static ChattingRoomCount zero() {
        return ZERO;
    }
}
