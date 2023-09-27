package touch.baton.assure.common;

import touch.baton.assure.oauth.OauthAssuredSupport;
import touch.baton.domain.oauth.command.OauthType;

public class OauthLoginTestManager {

    public String 소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(final String 테스트용_사용자_MockAuthCode) {
        OauthAssuredSupport
                .클라이언트_요청()
                .소셜_로그인을_위한_리다이렉트_URL을_요청한다(OauthType.GITHUB)

                .서버_응답()
                .소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다();

        final String 사용자_액세스_토큰 = OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, 테스트용_사용자_MockAuthCode)

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다()
                .액세스_토큰을_반환한다();

        return 사용자_액세스_토큰;
    }
}
