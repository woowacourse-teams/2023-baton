package touch.baton.assure.member;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.vo.SocialId;

import static touch.baton.assure.member.MemberAssuredSupport.로그인한_사용자_프로필_응답;

@SuppressWarnings("NonAsciiCharacters")
class MemberReadWithLoginedMemberAssuredTest extends AssuredTestConfig {

    @Test
    void 로그인_한_사용자_프로필을_조회한다() {
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Member 사용자_헤나 = memberRepository.getBySocialId(헤나_소셜_아이디);

        MemberAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(헤나_액세스_토큰)
                .사용자_본인_프로필을_가지고_있는_액세스_토큰으로_조회한다()

                .서버_응답()
                .로그인한_사용자_프로필_조회_성공을_검증한다(로그인한_사용자_프로필_응답(사용자_헤나));
    }
}
