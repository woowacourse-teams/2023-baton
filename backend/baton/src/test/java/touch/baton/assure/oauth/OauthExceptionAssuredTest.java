package touch.baton.assure.oauth;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.infra.auth.jwt.JwtConfig;
import touch.baton.infra.auth.jwt.JwtEncoder;

import java.util.Map;

class OauthExceptionAssuredTest extends AssuredTestConfig {

    @Test
    void 만료된_JWT_로_요청을_보내면_오류가_발생한다() {
        final JwtConfig 기간이_만료되게_하는_jwt_설정 = new JwtConfig("test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key", "test_issuer", -1);
        final JwtEncoder 만료된_jwt_인코더 = new JwtEncoder(기간이_만료되게_하는_jwt_설정);
        final String 기간_만료_엑세스_토큰 = 만료된_jwt_인코더.jwtToken(Map.of("socialId", MockAuthCodes.ethanAuthCode()));

        OauthAssuredSupport
                .클라이언트_요청()
                .엑세스_토큰으로_로그인_한다(기간_만료_엑세스_토큰)
                .기간이_만료된_엑세스_토큰으로_프로필_조회하려한다()

                .서버_응답()
                .토큰_기간_만료_오류가_발생한다();
    }
}
