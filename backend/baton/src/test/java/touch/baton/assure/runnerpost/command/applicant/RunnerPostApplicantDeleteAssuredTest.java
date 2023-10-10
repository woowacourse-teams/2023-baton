package touch.baton.assure.runnerpost.command.applicant;

import org.junit.jupiter.api.Test;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.runnerpost.support.command.applicant.RunnerPostApplicantCreateSupport;
import touch.baton.assure.runnerpost.support.command.applicant.RunnerPostApplicantDeleteAssuredSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.FakeAuthCodes;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.클라이언트_요청;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostApplicantDeleteAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_게시글에_보낸_리뷰_제안을_취소한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ditooAuthCode());
        final Long 디투_러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);

        // when
        서포터가_러너_게시글에_리뷰_신청을_성공한다(헤나_액세스_토큰, 디투_러너_게시글_식별자값);

        // then
        RunnerPostApplicantDeleteAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인_한다(헤나_액세스_토큰)
                .서포터가_리뷰_제안을_취소한다(디투_러너_게시글_식별자값)

                .서버_응답()
                .서포터의_리뷰_제안_철회를_검증한다(new HttpStatusAndLocationHeader(NO_CONTENT, "/api/v1/posts/runner/" + 디투_러너_게시글_식별자값));
    }

    private Long 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(final String 헤나_액세스_토큰) {
        return 클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(
                        러너_게시글_생성_요청(
                                "테스트용_러너_게시글_제목",
                                List.of("자바", "스프링"),
                                "https://test-pull-request.com",
                                LocalDateTime.now().plusHours(100),
                                "테스트용_러너_게시글_구현_내용",
                                "테스트용_러너_게시글_궁금한_내용",
                                "테스트용_러너_게시글_참고_사항"
                        )
                )

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다()
                .생성한_러너_게시글의_식별자값을_반환한다();
    }

    private void 서포터가_러너_게시글에_리뷰_신청을_성공한다(final String 서포터_액세스_토큰, final Long 러너_게시글_식별자값) {
        RunnerPostApplicantCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_액세스_토큰)
                .서포터가_러너_게시글에_리뷰를_신청한다(러너_게시글_식별자값, "안녕하세요. 서포터 헤나입니다.")

                .서버_응답()
                .서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(러너_게시글_식별자값);
    }
}
