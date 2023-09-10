package touch.baton.domain.runnerpost.vo;

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
public class PostscriptContents {

    @Column(name = "postscript_contents", nullable = false, columnDefinition = "TEXT")
    private String value;

    public PostscriptContents(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("PostscriptContents 객체 내부에 contents 는 null 일 수 없습니다.");
        }
    }
}
