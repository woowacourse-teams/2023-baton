package touch.baton.config.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class StringDateToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private final TimeZone timeZone;
    private final String dateTimeFormat;

    public StringDateToLocalDateTimeConverter(final String dateTimeFormat, final TimeZone timeZone) {
        this.timeZone = timeZone;
        this.dateTimeFormat = dateTimeFormat;
    }

    @Override
    public LocalDateTime convert(final String source) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat)
                .withZone(timeZone.toZoneId());

        return LocalDateTime.parse(source, formatter);
    }
}
