package touch.baton.config.converter;

import org.springframework.core.convert.converter.Converter;
import touch.baton.tobe.domain.oauth.command.OauthType;

public class OauthTypeConverter implements Converter<String, OauthType> {

    @Override
    public OauthType convert(final String source) {
        return OauthType.from(source);
    }
}
