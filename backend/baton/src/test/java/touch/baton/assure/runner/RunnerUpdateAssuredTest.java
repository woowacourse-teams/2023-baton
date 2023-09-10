package touch.baton.assure.runner;

import org.junit.jupiter.api.Test;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.runner.service.dto.RunnerUpdateRequest;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.assure.runner.RunnerAssuredSupport.러너_본인_프로필_수정_요청;
import static touch.baton.domain.common.exception.ClientErrorCode.COMPANY_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.NAME_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.RUNNER_INTRODUCTION_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.RUNNER_TECHNICAL_TAGS_ARE_NULL;

@SuppressWarnings("NonAsciiCharacters")
class RunnerUpdateAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_정보를_수정한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = 러너_본인_프로필_수정_요청("수정된_헤나", "수정된_회사", "수정된_러너_소개글", List.of("자바", "스프링"));

        // when, then
        RunnerAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_본인_프로필_수정_요청)

                .서버_응답()
                .러너_본인_프로필_수정_성공을_검증한다(new HttpStatusAndLocationHeader(NO_CONTENT, "/api/v1/profile/runner/me"));
    }

    @Test
    void 러너_정보_수정_시에_이름이_없으면_예외가_발생한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = 러너_본인_프로필_수정_요청(null, "수정된_회사", "수정된_러너_소개글", List.of("자바", "스프링"));

        // when, then
        RunnerAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_본인_프로필_수정_요청)

                .서버_응답()
                .러너_본인_프로필_수정_실패를_검증한다(NAME_IS_NULL);
    }

    @Test
    void 서포터_정보_수정_시에_소속이_없으면_예외가_발생한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when, then
        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = 러너_본인_프로필_수정_요청("수정된_헤나", null, "수정된_러너_소개글", List.of("자바", "스프링"));
        RunnerAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_본인_프로필_수정_요청)

                .서버_응답()
                .러너_본인_프로필_수정_실패를_검증한다(COMPANY_IS_NULL);
    }

    @Test
    void 러너_정보_수정_시에_소개글이_없으면_예외가_발생한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when, then
        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = 러너_본인_프로필_수정_요청("수정된_헤나", "수정된_회사", null, List.of("자바", "스프링"));
        RunnerAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_본인_프로필_수정_요청)

                .서버_응답()
                .러너_본인_프로필_수정_실패를_검증한다(RUNNER_INTRODUCTION_IS_NULL);
    }

    @Test
    void 러너_정보_수정_시에_기술_태그가_없으면_예외가_발생한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when, then
        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = 러너_본인_프로필_수정_요청("수정된_헤나", "수정된_회사", "수정된_러너_소개글", null);
        RunnerAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_본인_프로필_수정_요청)

                .서버_응답()
                .러너_본인_프로필_수정_실패를_검증한다(RUNNER_TECHNICAL_TAGS_ARE_NULL);
    }
}
