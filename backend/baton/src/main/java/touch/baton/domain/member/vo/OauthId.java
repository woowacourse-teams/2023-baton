package touch.baton.domain.member.vo;

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
public class OauthId {

    @Column(name = "oauth_id", nullable = false)
    private String value;

    public OauthId(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("OauthId 객체 내부에 oauthId 는 null 일 수 없습니다.");
        }
    }
}
