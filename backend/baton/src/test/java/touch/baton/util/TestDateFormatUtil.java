package touch.baton.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestDateFormatUtil {

    public static LocalDateTime createExpireDate(final LocalDateTime expireDate) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        final String formattedExpireDate = expireDate.format(formatter);

        return LocalDateTime.parse(formattedExpireDate, formatter);
    }
}
