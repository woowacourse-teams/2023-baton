package touch.baton.assure.runner;

import org.junit.jupiter.api.Test;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.service.dto.RunnerUpdateRequest;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.assure.runner.RunnerAssuredSupport.러너_본인_프로필_수정_요청;
import static touch.baton.assure.runner.RunnerAssuredSupport.러너_프로필_상세_응답;

@SuppressWarnings("NonAsciiCharacters")
class RunnerReadByRunnerIdAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_프로필_조회에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Runner 러너_헤나 = runnerRepository.getBySocialId(헤나_소셜_아이디);

        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = 러너_본인_프로필_수정_요청("수정된_헤나", "수정된_회사", "수정된_러너_소개글", List.of("자바", "스프링"));
        RunnerAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_본인_프로필_수정_요청)

                .서버_응답()
                .러너_본인_프로필_수정_성공을_검증한다(new HttpStatusAndLocationHeader(NO_CONTENT, "/api/v1/profile/runner/me"));

        // when, then
        RunnerAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_프로필을_상세_조회한다(러너_헤나.getId())

                .서버_응답()
                .러너_프로필_상세_조회를_검증한다(러너_프로필_상세_응답(러너_헤나, 러너_본인_프로필_수정_요청));
    }
}
