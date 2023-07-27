package touch.baton.config.converter;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private static final TimeZone KOREA_TIME_ZONE = TimeZone.getTimeZone("Asia/Seoul");

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new StringDateToLocalDateTimeConverter(DEFAULT_DATE_TIME_FORMAT, KOREA_TIME_ZONE));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeToStringDateConverter() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.timeZone(KOREA_TIME_ZONE);
            jacksonObjectMapperBuilder.simpleDateFormat(DEFAULT_DATE_TIME_FORMAT);

            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(formatter));
        };
    }
}
