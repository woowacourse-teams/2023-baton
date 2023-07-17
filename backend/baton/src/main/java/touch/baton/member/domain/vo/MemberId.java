package touch.baton.member.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class MemberId implements Serializable {

    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    public MemberId(final Long id) {
        this.id = id;
    }
}
