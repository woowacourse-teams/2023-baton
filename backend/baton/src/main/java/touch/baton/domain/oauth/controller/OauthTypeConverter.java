package touch.baton.domain.oauth.controller;

import org.springframework.core.convert.converter.Converter;
import touch.baton.domain.oauth.OauthType;

public class OauthTypeConverter implements Converter<String, OauthType> {

    @Override
    public OauthType convert(final String source) {
        return OauthType.from(source);
    }
}
