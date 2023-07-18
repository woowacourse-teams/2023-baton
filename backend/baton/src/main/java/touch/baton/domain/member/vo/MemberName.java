package touch.baton.domain.member.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class MemberName {

    @Column(name = "name", nullable = false)
    private String value;

    public MemberName(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("name 은 null 일 수 없습니다.");
        }
    }
}
