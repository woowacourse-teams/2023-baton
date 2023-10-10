package touch.baton.domain.notification.command.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class NotificationReferencedId {

    @Column(name = "referenced_id", nullable = false)
    private Long value;

    public NotificationReferencedId(final Long value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final Long value) {
        if (value == null) {
            throw new IllegalArgumentException("NotificationReferencedId 객체 내부에 referencedId 은 null 일 수 없습니다.");
        }
    }
}
