package touch.baton.domain.alarm.command.vo;

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
public class AlarmReferencedId {

    @Column(name = "referenced_id", nullable = false)
    private Long value;

    public AlarmReferencedId(final Long value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final Long value) {
        if (value == null) {
            throw new IllegalArgumentException("AlarmReferencedId 객체 내부에 alarmReferencedId 은 null 일 수 없습니다.");
        }
    }
}
