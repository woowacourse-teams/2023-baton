package touch.baton.domain.notification.command.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationReferencedIdTest {

    @DisplayName("value 가 null 이면 예외가 발생한다.")
    @Test
    void fail_if_value_is_null() {
        assertThatThrownBy(() -> new NotificationReferencedId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
