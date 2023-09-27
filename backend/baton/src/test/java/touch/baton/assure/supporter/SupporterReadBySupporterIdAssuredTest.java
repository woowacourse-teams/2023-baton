package touch.baton.assure.supporter;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;

import java.util.Collections;

import static touch.baton.assure.supporter.SupporterAssuredSupport.서포터_MyProfile_응답;
import static touch.baton.assure.supporter.SupporterAssuredSupport.서포터_Profile_응답;

@SuppressWarnings("NonAsciiCharacters")
class SupporterReadBySupporterIdAssuredTest extends AssuredTestConfig {

    @Test
    void 서포터_프로필을_조회한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Supporter 서포터_헤나 = supporterRepository.getBySocialId(헤나_소셜_아이디);

        // when, then
        SupporterAssuredSupport
                .클라이언트_요청()
                .서포터_프로필을_서포터_식별자값으로_조회한다(서포터_헤나.getId())

                .서버_응답()
                .서포터_프로필_조회_성공을_검증한다(서포터_Profile_응답(서포터_헤나, Collections.emptyList()));
    }

    @Test
    void 서포터_마이페이지_프로필을_조회한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Supporter 서포터_헤나 = supporterRepository.getBySocialId(헤나_소셜_아이디);

        // when, then
        SupporterAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(헤나_액세스_토큰)
                .서포터_마이페이지를_액세스_토큰으로_조회한다()

                .서버_응답()
                .서포터_마이페이지_프로필_조회_성공을_검증한다(서포터_MyProfile_응답(서포터_헤나, Collections.emptyList()));
    }
}
