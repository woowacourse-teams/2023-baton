package touch.baton.domain.tag.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.tag.command.vo.TagReducedName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TagReducedNameTest {

    @DisplayName("생성자의 매개변수로 들어온 notReducedValue 가 null 이면 예외가 발생한다.")
    @Test
    void construct_fail_if_value_is_null() {
        assertThatThrownBy(() -> TagReducedName.from(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("정적 팩토리 메소드로 생성 시에 value 의 공백은 제거된다.")
    @Test
    void reduce_blank_when_construct() {
        // given
        final String notReducedValue = "d i t o o";
        final String expected = "ditoo";

        // when
        final TagReducedName tagReducedName = TagReducedName.from(notReducedValue);

        // then
        assertThat(tagReducedName.getValue()).isEqualTo(expected);
    }

    @DisplayName("정적 팩토리 메소드로 생성 시에 value 는 모두 소문자로 변한다.")
    @Test
    void value_change_to_lower_case_when_construct() {
        // given
        final String notReducedValue = "DiToO";
        final String expected = "ditoo";

        // when
        final TagReducedName tagReducedName = TagReducedName.from(notReducedValue);

        // then
        assertThat(tagReducedName.getValue()).isEqualTo(expected);
    }
}
