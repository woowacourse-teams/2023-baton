package touch.baton.config.converter;

import org.springframework.core.convert.converter.Converter;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;

public class ReviewStatusConverter implements Converter<String, ReviewStatus> {

    @Override
    public ReviewStatus convert(final String source) {
        return ReviewStatus.from(source);
    }
}
