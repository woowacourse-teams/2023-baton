package touch.baton.assure.oauth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.token.ExpireDate;
import touch.baton.domain.oauth.token.Token;
import touch.baton.domain.oauth.token.Tokens;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.vo.ExpireDateFixture;
import touch.baton.infra.auth.jwt.JwtEncoder;

import java.time.LocalDateTime;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
class OauthRefreshTokenAssuredTest extends AssuredTestConfig {

    @Autowired
    private JwtEncoder jwtExpireEncoder;

    @Test
    void 만료된_JWT_로_본인_프로필_조회_요청을_보내면_오류가_발생한다() {
        final String 기간_만료_엑세스_토큰 = jwtExpireEncoder.jwtToken(Map.of("socialId", MockAuthCodes.ethanAuthCode()));

        OauthAssuredSupport
                .클라이언트_요청()
                .엑세스_토큰으로_로그인_한다(기간_만료_엑세스_토큰)
                .기간이_만료된_엑세스_토큰으로_프로필_조회하려_한다()

                .서버_응답()
                .오류가_발생한다(ClientErrorCode.JWT_CLAIM_IS_ALREADY_EXPIRED);
    }

    @Test
    void 만료된_JWT와_리프레시_토큰을_가지고_리프레시_요청을_보내면_성공한다() {
        OauthAssuredSupport
                .클라이언트_요청()
                .소셜_로그인을_위한_리다이렉트_URL을_요청한다(OauthType.GITHUB)

                .서버_응답()
                .소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다();

        final Tokens 액세스_토큰과_리프레시_토큰 = OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, MockAuthCodes.ethanAuthCode())

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다()
                .액세스_토큰과_리프레시_토큰을_반환한다(MemberFixture.createEthan());

        final String 기간_만료_액세스_토큰 = 기간_만료_액세스_토큰을_생성한다(액세스_토큰과_리프레시_토큰);
        final String 리프레시_토큰 = 리프레시_토큰을_가져온다(액세스_토큰과_리프레시_토큰);

        OauthAssuredSupport
                .클라이언트_요청()
                .기간_만료_액세스_토큰과_리프레시_토큰으로_리프레시_요청한다(기간_만료_액세스_토큰, 리프레시_토큰)

                .서버_응답()
                .새로운_액세스_토큰과_리프레시_토큰을_반환한다();
    }

    @Test
    void 만료되지_않은_JWT와_리프레시_토큰을_가지고_리프레시_요청을_보내면_실패한다() {
        OauthAssuredSupport
                .클라이언트_요청()
                .소셜_로그인을_위한_리다이렉트_URL을_요청한다(OauthType.GITHUB)

                .서버_응답()
                .소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다();

        final Tokens 액세스_토큰과_리프레시_토큰 = OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, MockAuthCodes.ethanAuthCode())

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다()
                .액세스_토큰과_리프레시_토큰을_반환한다(MemberFixture.createEthan());

        final String 유효_액세스_토큰 = 액세스_토큰과_리프레시_토큰.accessToken().getValue();
        final String 리프레시_토큰 = 리프레시_토큰을_가져온다(액세스_토큰과_리프레시_토큰);

        OauthAssuredSupport
                .클라이언트_요청()
                .기간_만료_액세스_토큰과_리프레시_토큰으로_리프레시_요청한다(유효_액세스_토큰, 리프레시_토큰)

                .서버_응답()
                .오류가_발생한다(ClientErrorCode.JWT_CLAIM_IS_NOT_EXPIRED);
    }

    @Test
    void 다른_사람의_JWT와_리프레시_토큰을_가지고_리프레시_요청을_보내면_실패한다() {
        OauthAssuredSupport
                .클라이언트_요청()
                .소셜_로그인을_위한_리다이렉트_URL을_요청한다(OauthType.GITHUB)

                .서버_응답()
                .소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다();

        final Tokens 헤나_액세스_토큰과_리프레시_토큰 = OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, MockAuthCodes.hyenaAuthCode())

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다()
                .액세스_토큰과_리프레시_토큰을_반환한다(MemberFixture.createHyena());

        final Tokens 에단_액세스_토큰과_리프레시_토큰 = OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, MockAuthCodes.ethanAuthCode())

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다()
                .액세스_토큰과_리프레시_토큰을_반환한다(MemberFixture.createEthan());

        final String 기간_만료_헤나_액세스_토큰 = 기간_만료_액세스_토큰을_생성한다(헤나_액세스_토큰과_리프레시_토큰);
        final String 에단_리프레시_토큰 = 리프레시_토큰을_가져온다(에단_액세스_토큰과_리프레시_토큰);

        OauthAssuredSupport
                .클라이언트_요청()
                .기간_만료_액세스_토큰과_리프레시_토큰으로_리프레시_요청한다(기간_만료_헤나_액세스_토큰, 에단_리프레시_토큰)

                .서버_응답()
                .오류가_발생한다(ClientErrorCode.ACCESS_TOKEN_AND_REFRESH_TOKEN_HAVE_DIFFERENT_OWNER);
    }

    private String 리프레시_토큰을_가져온다(final Tokens 액세스_토큰과_리프레시_토큰) {
        return 액세스_토큰과_리프레시_토큰.refreshToken().getToken().getValue();
    }

    @Test
    void 만료된_리프레시_토큰을_가지고_리프레시_요청을_보내면_실패한다() {
        OauthAssuredSupport
                .클라이언트_요청()
                .소셜_로그인을_위한_리다이렉트_URL을_요청한다(OauthType.GITHUB)

                .서버_응답()
                .소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다();

        final Tokens 액세스_토큰과_리프레시_토큰 = OauthAssuredSupport
                .클라이언트_요청()
                .AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(OauthType.GITHUB, MockAuthCodes.ethanAuthCode())

                .서버_응답()
                .AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다()
                .액세스_토큰과_리프레시_토큰을_반환한다(MemberFixture.createEthan());

        final String 만료된_액세스_토큰 = 기간_만료_액세스_토큰을_생성한다(액세스_토큰과_리프레시_토큰);
        final String 만료된_리프레시_토큰 = 만료된_리프레시_토큰을_가져온다(액세스_토큰과_리프레시_토큰);

        OauthAssuredSupport
                .클라이언트_요청()
                .기간_만료_액세스_토큰과_리프레시_토큰으로_리프레시_요청한다(만료된_액세스_토큰, 만료된_리프레시_토큰)

                .서버_응답()
                .오류가_발생한다(ClientErrorCode.REFRESH_TOKEN_IS_ALREADY_EXPIRED);
    }

    private String 기간_만료_액세스_토큰을_생성한다(final Tokens 액세스_토큰과_리프레시_토큰) {
        final SocialId 소셜_아이디 = 액세스_토큰과_리프레시_토큰.refreshToken().getMember().getSocialId();
        final String 기간_만료_액세스_토큰 = jwtExpireEncoder.jwtToken(Map.of("socialId", 소셜_아이디.getValue()));

        return 기간_만료_액세스_토큰;
    }

    private String 만료된_리프레시_토큰을_가져온다(final Tokens 액세스_토큰과_리프레시_토큰) {
        final Token 토큰 = 액세스_토큰과_리프레시_토큰.refreshToken().getToken();
        final ExpireDate 기간_만료일 = ExpireDateFixture.expireDate(LocalDateTime.now().minusDays(14));

        refreshTokenRepository.changeExpireDateByToken(토큰, 기간_만료일);

        return 토큰.getValue();
    }
}
