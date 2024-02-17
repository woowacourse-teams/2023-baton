package touch.baton.domain.member.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.member.command.vo.ReviewCount;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewCountTest {

    @DisplayName("increaseCount가 호출 되면 value가 1 증가한다.")
    @Test
    void increaseCount() {
        // given
        final int initialValue = 0;
        final ReviewCount reviewCount = new ReviewCount(initialValue);

        // when
        reviewCount.increase();

        // then
        assertThat(reviewCount.getValue()).isEqualTo(initialValue + 1);
    }
}
