package touch.baton.tobe.domain.oauth.command.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import touch.baton.config.converter.OauthTypeConverter;
import touch.baton.tobe.domain.oauth.command.OauthType;

import static org.assertj.core.api.Assertions.assertThat;

class OauthTypeConverterTest {

    @DisplayName("OauthType 이 github 으로 입력될 때 변환에 성공한다.")
    @ParameterizedTest
    @ValueSource(strings = {"github", "Github", "GitHub", "GITHUB"})
    void github(final String oauthTypeValue) {
        // given
        final OauthTypeConverter oauthTypeConverter = new OauthTypeConverter();

        // when
        final OauthType convertedOauthType = oauthTypeConverter.convert(oauthTypeValue);

        // then
        assertThat(convertedOauthType).isEqualTo(OauthType.GITHUB);
    }
}
