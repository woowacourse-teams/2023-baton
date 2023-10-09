package touch.baton.domain.notification.command.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;

class IsReadTest {

    @DisplayName("생성 성공한다")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void success(final boolean value) {
        assertThatCode(() -> new IsRead(value))
                .doesNotThrowAnyException();
    }
}
