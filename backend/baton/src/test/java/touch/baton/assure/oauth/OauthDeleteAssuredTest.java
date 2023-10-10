package touch.baton.assure.oauth;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.FakeAuthCodes;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.oauth.command.OauthType;
import touch.baton.domain.oauth.command.token.Tokens;
import touch.baton.fixture.domain.MemberFixture;

@SuppressWarnings("NonAsciiCharacters")
class OauthDeleteAssuredTest extends AssuredTestConfig {

    @Test
    void 로그아웃을_성공한다() {
        // given
        OauthAssuredSupport
                .클라이언트_요청()
                .소셜_로그인을_위한_리다이렉트_URL을_요청한다(OauthType.GITHUB)

                .서버_응답()
                .소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다();

        final Tokens 액세스_토큰과_리프레시_토큰 = OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, FakeAuthCodes.ethanAuthCode())

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다()
                .액세스_토큰과_리프레시_토큰을_반환한다(MemberFixture.createEthan());

        // when, then
        OauthAssuredSupport
                .클라이언트_요청()
                .로그아웃을_요청한다(액세스_토큰과_리프레시_토큰.accessToken())

                .서버_응답()
                .로그아웃이_성공한다();
    }

    @Test
    void 액세스_토큰이_없이_로그아웃을_요청하면_실패한다() {
        // given
        OauthAssuredSupport
                .클라이언트_요청()
                .소셜_로그인을_위한_리다이렉트_URL을_요청한다(OauthType.GITHUB)

                .서버_응답()
                .소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다();

        final Tokens 액세스_토큰과_리프레시_토큰 = OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, FakeAuthCodes.ethanAuthCode())

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다()
                .액세스_토큰과_리프레시_토큰을_반환한다(MemberFixture.createEthan());

        // when, then
        OauthAssuredSupport
                .클라이언트_요청()
                .액세스_토큰_없이_로그아웃을_요청한다()

                .서버_응답()
                .오류가_발생한다(ClientErrorCode.OAUTH_AUTHORIZATION_VALUE_IS_NULL);
    }
}
