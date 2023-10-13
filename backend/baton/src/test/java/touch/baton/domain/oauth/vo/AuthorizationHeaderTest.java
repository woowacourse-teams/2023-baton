package touch.baton.domain.oauth.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.oauth.command.AuthorizationHeader;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class AuthorizationHeaderTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> new AuthorizationHeader("hello"))
                    .doesNotThrowAnyException();
        }

        @DisplayName("value 가 null 이면 예외가 발생한다.")
        @Test
        void fail_if_value_is_null() {
            assertThatThrownBy(() -> new AuthorizationHeader(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("Bearer 로 파싱할 수 있다.")
    @Test
    void parseBearerAccessToken() {
        // given
        final String expected = "accessToken";
        final AuthorizationHeader authorizationHeader = new AuthorizationHeader("Bearer " + expected);

        // when
        final String actual = authorizationHeader.parseBearerAccessToken();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Bearer 로 시작하는지 검증한다.")
    @Test
    void isNotBearerAuth() {
        // given
        final AuthorizationHeader bearerAuthorizationHeader = new AuthorizationHeader("Bearer " + "accessToken");
        final AuthorizationHeader notBearerAuthorizationHeader = new AuthorizationHeader("Basic " + "accessToken");

        // when, then
        assertSoftly(softly -> {
            softly.assertThat(bearerAuthorizationHeader.isNotBearerAuth()).isFalse();
            softly.assertThat(notBearerAuthorizationHeader.isNotBearerAuth()).isTrue();
        });
    }
}
