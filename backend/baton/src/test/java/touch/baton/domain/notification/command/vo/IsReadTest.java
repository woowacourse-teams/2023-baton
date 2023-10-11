package touch.baton.domain.notification.command.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class IsReadTest {

    @DisplayName("정적 팩터리 메서드로 읽음 여부 false 생성에 성공한다")
    @Test
    void success_create_isRead_false() {
        assertThatCode(() -> IsRead.asUnRead())
                .doesNotThrowAnyException();
    }

    @DisplayName("정적 팩터리 메서드로 읽음 여부 false 를 생성 할 수 있다.")
    @Test
    void success_isRead_false() {
        // given
        final IsRead actual = IsRead.asUnRead();

        // when & then
        assertThat(actual.getValue()).isFalse();
    }

    @DisplayName("정적 팩터리 메서드로 읽음 여부 true 생성에 성공한다")
    @Test
    void success_create_isRead_true() {
        assertThatCode(() -> IsRead.asRead())
                .doesNotThrowAnyException();
    }

    @DisplayName("정적 팩터리 메서드로 읽음 여부 true 를 생성 할 수 있다.")
    @Test
    void success_isRead_true() {
        // given
        final IsRead actual = IsRead.asRead();

        // when & then
        assertThat(actual.getValue()).isTrue();
    }
}
