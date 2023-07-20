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
public class PullRequestUrl {

    private static final int MAXIMUM_URL_LENGTH = 2083;

    @Column(name = "pull_request_url", nullable = false, length = MAXIMUM_URL_LENGTH)
    private String value;

    public PullRequestUrl(final String value) {
        validateNotNull(value);
        this.value = value;
    }

    private void validateNotNull(final String value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("pull request url 은 null 일 수 없습니다.");
        }
    }
}
