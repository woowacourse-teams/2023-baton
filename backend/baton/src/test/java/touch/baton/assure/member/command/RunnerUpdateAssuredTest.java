package touch.baton.assure.member.command;

import org.junit.jupiter.api.Test;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.member.support.command.RunnerUpdateAssuredSupport;
import touch.baton.assure.member.support.query.RunnerQueryAssuredSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.service.dto.RunnerUpdateRequest;
import touch.baton.domain.member.command.vo.SocialId;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.assure.member.support.command.RunnerUpdateAssuredSupport.러너_본인_프로필_수정_요청;
import static touch.baton.assure.member.support.query.RunnerQueryAssuredSupport.RunnerQueryResponseBuilder.러너_프로필_상세_응답;
import static touch.baton.domain.common.exception.ClientErrorCode.*;

@SuppressWarnings("NonAsciiCharacters")
class RunnerUpdateAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_정보를_수정한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = RunnerUpdateAssuredSupport.러너_본인_프로필_수정_요청("수정된_헤나", "수정된_회사", "수정된_러너_소개글", List.of("자바", "스프링"));

        // when, then
        RunnerUpdateAssuredSupport
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

        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = RunnerUpdateAssuredSupport.러너_본인_프로필_수정_요청(null, "수정된_회사", "수정된_러너_소개글", List.of("자바", "스프링"));

        // when, then
        RunnerUpdateAssuredSupport
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
        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = RunnerUpdateAssuredSupport.러너_본인_프로필_수정_요청("수정된_헤나", null, "수정된_러너_소개글", List.of("자바", "스프링"));
        RunnerUpdateAssuredSupport
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
        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = RunnerUpdateAssuredSupport.러너_본인_프로필_수정_요청("수정된_헤나", "수정된_회사", null, List.of("자바", "스프링"));
        RunnerUpdateAssuredSupport
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
        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = RunnerUpdateAssuredSupport.러너_본인_프로필_수정_요청("수정된_헤나", "수정된_회사", "수정된_러너_소개글", null);
        RunnerUpdateAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_본인_프로필_수정_요청)

                .서버_응답()
                .러너_본인_프로필_수정_실패를_검증한다(RUNNER_TECHNICAL_TAGS_ARE_NULL);
    }

    @Test
    void 수정된_러너_프로필_조회에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Runner 러너_헤나 = runnerRepository.getBySocialId(헤나_소셜_아이디);

        final RunnerUpdateRequest 러너_본인_프로필_수정_요청 = 러너_본인_프로필_수정_요청("수정된_헤나", "수정된_회사", "수정된_러너_소개글", List.of("자바", "스프링"));
        RunnerUpdateAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_본인_프로필_수정_요청)

                .서버_응답()
                .러너_본인_프로필_수정_성공을_검증한다(new HttpStatusAndLocationHeader(NO_CONTENT, "/api/v1/profile/runner/me"));

        // when, then
        RunnerQueryAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_프로필을_상세_조회한다(러너_헤나.getId())

                .서버_응답()
                .러너_프로필_상세_조회를_검증한다(러너_프로필_상세_응답(러너_헤나, 러너_본인_프로필_수정_요청));
    }
}
