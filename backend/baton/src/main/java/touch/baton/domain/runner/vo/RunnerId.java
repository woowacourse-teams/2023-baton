package touch.baton.domain.runner.vo;

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
public class RunnerId implements Serializable {


    public RunnerId(final Long id) {
        this.id = id;
    }

    @GeneratedValue(strategy = IDENTITY)
    private Long id;
}
