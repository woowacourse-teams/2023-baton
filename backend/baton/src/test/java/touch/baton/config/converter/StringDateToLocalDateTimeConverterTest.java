package touch.baton.config.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringDateToLocalDateTimeConverterTest {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private static final TimeZone KOREA_TIME_ZONE = TimeZone.getTimeZone("Asia/Seoul");

    @DisplayName("String 값 Date 가 LocalDateTime 으로 변경되는지 확인한다.")
    @Test
    void success_convertStringDateToLocalDateTime() {
        // given
        final String expect = "2023-08-18T09:45";
        final StringDateToLocalDateTimeConverter converter = new StringDateToLocalDateTimeConverter(DEFAULT_DATE_TIME_FORMAT, KOREA_TIME_ZONE);

        // when
        final LocalDateTime actual = converter.convert(expect);

        // then
        assertThat(actual).isEqualTo(LocalDateTime.of(2023, 8, 18, 9, 45));
    }

    @DisplayName("형식에 맞지 않는 String 값 Date 는 convert 할 때 예외를 던진다.")
    @ValueSource(strings = {"2023-08-18T09:45:12", "2023-08-18 09:45", "2023/08/18 09:45", "2023.08.18 09:45"})
    @ParameterizedTest
    void fail_convertInvalidStringDateToLocalDateTime(final String expect) {
        // given
        final StringDateToLocalDateTimeConverter converter = new StringDateToLocalDateTimeConverter(DEFAULT_DATE_TIME_FORMAT, KOREA_TIME_ZONE);

        // when, then
        assertThatThrownBy(() -> converter.convert(expect)).isInstanceOf(DateTimeParseException.class);
    }
}
