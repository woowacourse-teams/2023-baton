package touch.baton.config.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.MockMvcTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@MockMvcTest(value = ConverterConfig.class)
class ConverterConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("직렬화 할 때 LocalDateTime yyyy-MM-dd'T'HH:mm 형식으로 변경된다.")
    @Test
    void serializeStringDateToLocalDateTime() throws JsonProcessingException {
        // given
        final LocalDateTime expected = LocalDateTime.of(2023, 8, 15, 12, 13);
        final LocalDateTime expectedWithSecond = LocalDateTime.of(2023, 8, 15, 12, 13, 12);

        // when
        final String actual = objectMapper.writeValueAsString(expected);
        final String actualWithSecond = objectMapper.writeValueAsString(expectedWithSecond);

        // then
        assertAll(
                () -> assertThat(actual).isEqualTo("\"2023-08-15T12:13\""),
                () -> assertThat(actualWithSecond).isEqualTo("\"2023-08-15T12:13\"")
        );
    }

    @DisplayName("역직렬화 할 때, yyyy-MM-dd'T'HH:mm 형식이 LocalDateTime 으로 변경된다.")
    @Test
    void success_deserializeLocalDateTimeToStringDate() throws JsonProcessingException {
        // given
        final String expected = "\"2023-08-15T12:13\"";

        // when
        final LocalDateTime actual = objectMapper.readValue(expected, LocalDateTime.class);

        // then
        assertThat(actual).isEqualTo(LocalDateTime.of(2023, 8, 15, 12, 13));
    }

    @DisplayName("역직렬화 할 때, 맞지 않는 형식의 날짜 String 이 들어오면 LocalDateTime 으로 변환이 실패한다.")
    @ValueSource(strings = {"\"2023-08-15T12:13:45\"", "\"2023-08-15 12:13\"", "\"2023/08/15T12:13\"", "\"2023/08/15 12:13\""})
    @ParameterizedTest
    void fail_deserializeLocalDateTimeToStringDate(final String expected) {
        // when, then
        assertThatThrownBy(() -> objectMapper.readValue(expected, LocalDateTime.class))
                .isInstanceOf(InvalidFormatException.class);
    }
}
