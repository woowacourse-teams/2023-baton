package touch.baton.domain.member.command.vo;

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
public class MemberName {

    private static final String DEFAULT_VALUE = "익명의 사용자";

    @Column(name = "name", nullable = false)
    private String value = DEFAULT_VALUE;

    public MemberName(final String value) {
        if (Objects.isNull(value)) {
            return;
        }
        this.value = value;
    }
}
