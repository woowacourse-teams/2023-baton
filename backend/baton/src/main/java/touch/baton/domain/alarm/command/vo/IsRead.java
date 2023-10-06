package touch.baton.domain.alarm.command.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class IsRead {

    @ColumnDefault(value = "false")
    @Column(name = "is_read", nullable = false)
    private boolean value = false;

    public IsRead(final boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
