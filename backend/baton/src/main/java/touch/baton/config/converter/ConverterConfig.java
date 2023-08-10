package touch.baton.config.converter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
        registry.addConverter(new OauthTypeConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(jacksonBuilder().build());
        return converter;
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        builder.timeZone(KOREA_TIME_ZONE);
        builder.simpleDateFormat(DEFAULT_DATE_TIME_FORMAT);

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
        builder.serializers(new LocalDateTimeSerializer(formatter));
        builder.deserializers(new LocalDateTimeDeserializer(formatter));
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return builder;
    }
}
