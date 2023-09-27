package touch.baton.assure.runnerpost.command;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport;
import touch.baton.assure.runnerpost.support.command.RunnerPostDeleteSupport;
import touch.baton.assure.runnerpost.support.command.RunnerPostUpdateSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.support.command.applicant.RunnerPostApplicantCreateSupport.러너의_서포터_선택_요청;
import static touch.baton.assure.runnerpost.support.command.applicant.RunnerPostApplicantCreateSupport.클라이언트_요청;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostDeleteAssuredTest extends AssuredTestConfig {

    @Test
    void 리뷰가_대기중이고_리뷰_지원자가_없다면_러너의_게시글_식별자값으로_러너_게시글_삭제에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());
        final Long 러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(헤나_액세스_토큰);

        // when, then
        RunnerPostDeleteSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글_삭제_성공을_검증한다(NO_CONTENT);
    }

    @Test
    void 러너_게시글이_존재하지_않으면_러너의_게시글_식별자값으로_러너_게시글_삭제에_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        // when
        final Long 존재하지_않는_러너_게시글의_식별자값 = -1L;

        // then
        RunnerPostDeleteSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(존재하지_않는_러너_게시글의_식별자값)

                .서버_응답()
                .러너_게시글_삭제_실패를_검증한다(INTERNAL_SERVER_ERROR);
    }

    @Test
    void 리뷰가_진행중인_상태라면_러너의_게시글_식별자값으로_러너_게시글_삭제에_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.ditooAuthCode());
        final Long 디투_러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);

        // when
        서포터가_러너_게시글에_리뷰_신청을_성공한다(헤나_액세스_토큰, 디투_러너_게시글_식별자값);

        // then
        RunnerPostDeleteSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(디투_러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글_삭제_실패를_검증한다(INTERNAL_SERVER_ERROR);
    }

    @Test
    void 리뷰가_완료된_상태라면_러너의_게시글_식별자값으로_러너_게시글_삭제에_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());
        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Supporter 서포터_헤나 = supporterRepository.getBySocialId(헤나_소셜_아이디);

        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.ditooAuthCode());
        final Long 디투_러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);

        // when
        서포터가_러너_게시글에_리뷰_신청을_성공한다(헤나_액세스_토큰, 디투_러너_게시글_식별자값);
        러너가_서포터의_리뷰_신청_선택에_성공한다(서포터_헤나, 디투_액세스_토큰, 디투_러너_게시글_식별자값);
        서포터가_러너_게시글의_리뷰를_완료로_변경하는_것을_성공한다(헤나_액세스_토큰, 디투_러너_게시글_식별자값);

        // then
        RunnerPostDeleteSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(디투_러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글_삭제_실패를_검증한다(INTERNAL_SERVER_ERROR);
    }

    @Test
    void 리뷰_요청_대기중인_상태이고_리뷰_지원자가_있는_상태라면_러너의_게시글_식별자값으로_러너_게시글_삭제에_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.ditooAuthCode());
        final Long 디투_러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);

        // when
        서포터가_러너_게시글에_리뷰_신청을_성공한다(헤나_액세스_토큰, 디투_러너_게시글_식별자값);

        // then
        RunnerPostDeleteSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(디투_러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글_삭제_실패를_검증한다(INTERNAL_SERVER_ERROR);
    }

    private Long 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(final String 헤나_액세스_토큰) {
        return RunnerPostCreateSupport
                .클라이언트_요청()
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

    private void 서포터가_러너_게시글에_리뷰_신청을_성공한다(final String 헤나_액세스_토큰, final Long 디투_러너_게시글_식별자값) {
        클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .서포터가_러너_게시글에_리뷰를_신청한다(디투_러너_게시글_식별자값, "안녕하세요. 서포터 헤나입니다.")

                .서버_응답()
                .서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(디투_러너_게시글_식별자값);
    }

    private void 러너가_서포터의_리뷰_신청_선택에_성공한다(final Supporter 서포터_헤나, final String 디투_액세스_토큰, final Long 디투_러너_게시글_식별자값) {
        RunnerPostUpdateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(디투_액세스_토큰)
                .러너가_서포터를_선택한다(디투_러너_게시글_식별자값, 러너의_서포터_선택_요청(서포터_헤나.getId()))

                .서버_응답()
                .러너_게시글에_서포터가_성공적으로_선택되었는지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }

    private void 서포터가_러너_게시글의_리뷰를_완료로_변경하는_것을_성공한다(final String 헤나_액세스_토큰, final Long 디투_러너_게시글_식별자값) {
        RunnerPostUpdateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .서포터가_리뷰를_완료하고_리뷰완료_버튼을_누른다(디투_러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글이_성공적으로_리뷰_완료_상태인지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }
}
