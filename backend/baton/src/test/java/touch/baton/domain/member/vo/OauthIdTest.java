package touch.baton.domain.member.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.tobe.domain.member.command.vo.OauthId;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OauthIdTest {

    @DisplayName("value 가 null 이면 예외가 발생한다.")
    @Test
    void fail_if_value_is_null() {
        assertThatThrownBy(() -> new OauthId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
