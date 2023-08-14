package touch.baton.domain.runnerpost.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MINUTES;
import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Deadline {

    @Column(name = "deadline", nullable = false)
    private LocalDateTime value;

    public Deadline(final LocalDateTime value) {
        validateNotNull(value);
        this.value = value.truncatedTo(MINUTES);
    }

    private void validateNotNull(final LocalDateTime value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("Deadline 객체 내부에 deadline 은 null 일 수 없습니다.");
        }
    }

    public boolean isEnd() {
        return value.isBefore(LocalDateTime.now());
    }
}
