package touch.baton.assure.member.query;

import org.junit.jupiter.api.Test;
import touch.baton.assure.member.support.command.RunnerUpdateAssuredSupport;
import touch.baton.assure.member.support.query.RunnerQueryAssuredSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.vo.SocialId;

import java.util.Collections;

@SuppressWarnings("NonAsciiCharacters")
class RunnerQueryAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_본인_프로필을_가지고_있는_액세스_토큰으로_조회에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Runner 러너_헤나 = runnerRepository.getBySocialId(헤나_소셜_아이디);

        // when, then
        RunnerQueryAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_가지고_있는_액세스_토큰으로_조회한다()

                .서버_응답()
                .러너_본인_프로필_조회_성공을_검증한다(RunnerUpdateAssuredSupport.러너_본인_프로필_응답(러너_헤나, Collections.emptyList()));
    }
}
