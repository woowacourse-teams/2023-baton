package touch.baton.domain.common.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.feedback.command.vo.Description;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DescriptionTest {

    @DisplayName("value 가 null 이면 예외가 발생한다.")
    @Test
    void fail_if_value_is_null() {
        assertThatThrownBy(() -> new Description(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Description 객체 내부에 value 는 null 일 수 없습니다.");
    }
}
