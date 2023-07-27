package touch.baton.config.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(ConverterConfig.class)
class ConverterConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("직렬화 할 때 LocalDateTime yyyy-MM-dd'T'HH:mm 형식으로 변경된다.")
    @Test
    void serializeStringDateToLocalDateTime() throws JsonProcessingException {
        // given
        final LocalDateTime expected = LocalDateTime.of(2023, 8, 15, 12, 13, 12);

        // when
        final String actual = objectMapper.writeValueAsString(expected);

        // then
        assertThat(actual).isEqualTo("\"2023-08-15T12:13\"");
    }
}
