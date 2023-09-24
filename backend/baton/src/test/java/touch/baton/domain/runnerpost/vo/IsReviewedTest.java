package touch.baton.domain.runnerpost.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IsReviewedTest {

    @DisplayName("기본 생성의 default 값은 false 이다.")
    @Test
    void default_is_false() {
        // given
        final IsReviewed isReviewed = new IsReviewed();

        // expect
        final boolean actual = isReviewed.getValue();

        assertThat(actual).isFalse();
    }
}
