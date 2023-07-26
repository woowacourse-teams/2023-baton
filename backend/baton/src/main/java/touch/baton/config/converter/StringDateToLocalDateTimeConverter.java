package touch.baton.config.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class StringDateToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private static final String SEOUL = "Asia/Seoul";

    @Override
    public LocalDateTime convert(final String source) {
        final TimeZone koreaTimeZone = TimeZone.getTimeZone(SEOUL);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)
                .withZone(koreaTimeZone.toZoneId());

        return LocalDateTime.parse(source, formatter);
    }
}
