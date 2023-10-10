package touch.baton.domain.notification.command.vo;

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

    private IsRead(final boolean value) {
        this.value = value;
    }

    public static IsRead asRead() {
        return new IsRead(true);
    }

    public static IsRead asUnRead() {
        return new IsRead(false);
    }

    public boolean getValue() {
        return value;
    }
}
