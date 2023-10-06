package touch.baton.domain.alarm.command.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;
import static touch.baton.domain.alarm.command.vo.AlarmText.MESSAGE_REFERENCED_BY_RUNNER_POST;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class AlarmMessage {

    @Column(name = "message", nullable = false)
    private String value;

    public AlarmMessage(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    public static AlarmMessage fromRunnerPost(final String value) {
        return new AlarmMessage(String.format(MESSAGE_REFERENCED_BY_RUNNER_POST.getText(), value));
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("AlarmMessage 객체 내부에 message 는 null 일 수 없습니다.");
        }
    }
}
