package touch.baton.domain.runnerpost.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Deadline {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    @Column(name = "deadline", nullable = false)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime value;

    public Deadline(final LocalDateTime value) {
        validateNotNull(value);
        this.value = value;
    }

    public static Deadline from(final String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        try {
            return new Deadline(LocalDateTime.parse(input, formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("deadline 의 형식이 잘못되었습니다.");
        }
    }

    private void validateNotNull(final LocalDateTime value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("deadline 은 null 일 수 없습니다.");
        }
    }
}
