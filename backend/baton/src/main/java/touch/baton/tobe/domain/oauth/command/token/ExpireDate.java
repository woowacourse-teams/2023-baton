package touch.baton.tobe.domain.oauth.command.token;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class ExpireDate {

    @Column(name = "expire_date", nullable = false)
    private LocalDateTime value;

    public ExpireDate(final LocalDateTime value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("ExpireDate 의 value 는 null일 수 없습니다.");
        }
    }

    public void refreshExpireTokenDate(final int minutes) {
        this.value = LocalDateTime.now().plusMinutes(minutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(value);
    }
}
