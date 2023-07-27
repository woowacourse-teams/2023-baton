package touch.baton.config.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

class StringDateToLocalDateTimeConverterTest {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private static final TimeZone KOREA_TIME_ZONE = TimeZone.getTimeZone("Asia/Seoul");

    @DisplayName("String 값 Date 가 LocalDateTime 으로 변경되는지 확인한다.")
    @Test
    void convertStringDateToLocalDateTime() {
        // given
        final String expect = "2023-08-18T09:45";
        final StringDateToLocalDateTimeConverter converter = new StringDateToLocalDateTimeConverter(DEFAULT_DATE_TIME_FORMAT, KOREA_TIME_ZONE);

        // when
        final LocalDateTime actual = converter.convert(expect);

        // then
        assertThat(actual).isEqualTo(LocalDateTime.of(2023, 8, 18, 9, 45));
    }
}
