package touch.baton.assure.oauth;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.oauth.command.OauthType;

@SuppressWarnings("NonAsciiCharacters")
class OauthCreateAssuredTest extends AssuredTestConfig {

    @Test
    void 사용자는_소셜_회원가입을_성공한다() {
        OauthAssuredSupport
                .클라이언트_요청()
                .소셜_로그인을_위한_리다이렉트_URL을_요청한다(OauthType.GITHUB)

                .서버_응답()
                .소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다();

        OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, MockAuthCodes.hyenaAuthCode())

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다();
    }
}
