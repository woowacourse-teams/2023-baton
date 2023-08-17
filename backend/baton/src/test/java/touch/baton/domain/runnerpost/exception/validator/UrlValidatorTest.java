package touch.baton.domain.runnerpost.exception.validator;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UrlValidatorTest {

    @DisplayName("URL을 검증한다.")
    @Nested
    class isValid {

        @DisplayName("옳은 URL이면 통과한다.")
        @ValueSource(strings = {"https://www.naver.com",
                "https://www.naver.com",
                "https://github.com/cookienc",
                "http://dev-king-ethan.n-e.kr",
                "https://www.naver.com/hello",
                "https://www.naver.com/hello?world=123",
                "https://github.com/twitter/the-algorithm/pull/1740"
        })
        @ParameterizedTest
        void success(final String target) {
            // given
            final UrlValidator urlValidator = new UrlValidator();
            urlValidator.initialize(new MockValidNotUrl());
            final MockConstraintValidatorContext mockconstraintValidatorContext = new MockConstraintValidatorContext();

            // when, then
            assertThat(urlValidator.isValid(target, mockconstraintValidatorContext)).isTrue();
        }

        @DisplayName("옳지 않은 URL이면 통과하지 않는다.")
        @ValueSource(strings = {"https;//github.com/hello",
                "https://",
                "http://",
                "URL 아님",
                "github.com/twitter/the-algorithm/pull/1740",
                "htts:github.com/twitter/the-algorithm/pull/1740"
        })
        @ParameterizedTest
        void fail_if_not_url(final String target) {
            // given
            final UrlValidator urlValidator = new UrlValidator();
            urlValidator.initialize(new MockValidNotUrl());
            final MockConstraintValidatorContext mockConstraintValidatorContext = new MockConstraintValidatorContext();

            // when, then
            assertThatThrownBy(() -> urlValidator.isValid(target, mockConstraintValidatorContext))
                    .isInstanceOf(ClientRequestException.class);
        }
    }

    private static class MockConstraintValidatorContext implements ConstraintValidatorContext {

        @Override
        public void disableDefaultConstraintViolation() {

        }

        @Override
        public String getDefaultConstraintMessageTemplate() {
            return null;
        }

        @Override
        public ClockProvider getClockProvider() {
            return null;
        }

        @Override
        public ConstraintViolationBuilder buildConstraintViolationWithTemplate(final String messageTemplate) {
            return null;
        }

        @Override
        public <T> T unwrap(final Class<T> type) {
            return null;
        }
    }

    private static class MockValidNotUrl implements ValidNotUrl {

        @Override
        public String message() {
            return null;
        }

        @Override
        public Class<?>[] groups() {
            return null;
        }

        @Override
        public Class<? extends Payload>[] payload() {
            return null;
        }

        @Override
        public ClientErrorCode clientErrorCode() {
            return ClientErrorCode.PULL_REQUEST_URL_IS_NOT_URL;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }

}
