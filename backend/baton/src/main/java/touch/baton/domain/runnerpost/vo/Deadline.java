package touch.baton.domain.runnerpost.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Deadline {

    @Column(name = "deadline", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime value;

    public Deadline(final LocalDateTime value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final LocalDateTime value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("deadline 은 null 일 수 없습니다.");
        }
    }
}
