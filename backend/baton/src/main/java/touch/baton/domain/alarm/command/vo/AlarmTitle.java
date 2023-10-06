package touch.baton.domain.alarm.command.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class AlarmTitle {

    @Column(name = "title", nullable = false)
    private String value;

    public AlarmTitle(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    public static AlarmTitle fromAppliedSupporter() {
        return new AlarmTitle("서포터의 제안이 왔습니다.");
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("AlarmTitle 객체 내부에 title 은 null 일 수 없습니다.");
        }
    }
}
