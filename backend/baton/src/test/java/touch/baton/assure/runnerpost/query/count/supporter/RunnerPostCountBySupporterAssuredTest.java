package touch.baton.assure.runnerpost.query.count.supporter;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport;
import touch.baton.assure.runnerpost.support.command.RunnerPostUpdateSupport;
import touch.baton.assure.runnerpost.support.query.count.supporter.RunnerPostCountBySupporterSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.FakeAuthCodes;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.support.command.applicant.RunnerPostApplicantCreateSupport.러너의_서포터_선택_요청;
import static touch.baton.assure.runnerpost.support.command.applicant.RunnerPostApplicantCreateSupport.클라이언트_요청;
import static touch.baton.assure.runnerpost.support.query.count.supporter.RunnerPostCountBySupporterSupport.러너_게시글_개수_응답;

@SuppressWarnings("NonAsciiCharacters")
    class RunnerPostCountBySupporterAssuredTest extends AssuredTestConfig {

    @Test
    void 로그인한_서포터가_지원했으면서_아직_시작하지_않은_러너_게시글_개수_조회에_성공한다() {
        // given
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ditooAuthCode());
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());
        final String 서포터_에단_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ethanAuthCode());
        final Long 디투_러너_게시글_식별자_값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);

        러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(헤나_액세스_토큰);

        서포터가_러너_게시글에_리뷰_신청을_성공한다(서포터_에단_액세스_토큰, 디투_러너_게시글_식별자_값);

        // when
        final RunnerPostResponse.Count 기대된_러너_게시글_개수 = 러너_게시글_개수_응답(1);

        // then
        RunnerPostCountBySupporterSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_에단_액세스_토큰)
                .리뷰_상태로_로그인한_서포터와_연관된_러너_게시글_개수를_조회한다(ReviewStatus.NOT_STARTED)

                .서버_응답()
                .서포터와_연관된_러너_게시글_개수_조회_성공을_검증한다(기대된_러너_게시글_개수);
    }

    @Test
    void 로그인한_서포터가_리뷰_중인_러너_게시글_개수_조회에_성공한다() {
        // given
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ditooAuthCode());
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());
        final String 서포터_에단_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ethanAuthCode());
        final SocialId 서포터_에단_소셜_아이디 = jwtTestManager.parseToSocialId(서포터_에단_액세스_토큰);
        final Supporter 서포터_에단 = supporterRepository.getBySocialId(서포터_에단_소셜_아이디);

        final Long 디투_러너_게시글_식별자_값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);
        final Long 헤나_러너_게시글_식별자_값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(헤나_액세스_토큰);

        서포터가_러너_게시글에_리뷰_신청을_성공한다(서포터_에단_액세스_토큰, 헤나_러너_게시글_식별자_값);

        서포터가_러너_게시글에_리뷰_신청을_성공한다(서포터_에단_액세스_토큰, 디투_러너_게시글_식별자_값);
        러너가_서포터의_리뷰_신청_선택에_성공한다(서포터_에단, 디투_액세스_토큰, 디투_러너_게시글_식별자_값);

        // when
        final RunnerPostResponse.Count 기대된_러너_게시글_개수 = 러너_게시글_개수_응답(1);

        // then
        RunnerPostCountBySupporterSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_에단_액세스_토큰)
                .리뷰_상태로_로그인한_서포터와_연관된_러너_게시글_개수를_조회한다(ReviewStatus.IN_PROGRESS)

                .서버_응답()
                .서포터와_연관된_러너_게시글_개수_조회_성공을_검증한다(기대된_러너_게시글_개수);
    }

    @Test
    void 로그인한_서포터가_완료한_러너_게시글_개수_조회에_성공한다() {
        // given
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ditooAuthCode());
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());
        final String 서포터_에단_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ethanAuthCode());
        final SocialId 서포터_에단_소셜_아이디 = jwtTestManager.parseToSocialId(서포터_에단_액세스_토큰);
        final Supporter 서포터_에단 = supporterRepository.getBySocialId(서포터_에단_소셜_아이디);

        final Long 디투_러너_게시글_식별자_값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);
        final Long 헤나_러너_게시글_식별자_값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(헤나_액세스_토큰);

        서포터가_러너_게시글에_리뷰_신청을_성공한다(서포터_에단_액세스_토큰, 헤나_러너_게시글_식별자_값);

        서포터가_러너_게시글에_리뷰_신청을_성공한다(서포터_에단_액세스_토큰, 디투_러너_게시글_식별자_값);
        러너가_서포터의_리뷰_신청_선택에_성공한다(서포터_에단, 디투_액세스_토큰, 디투_러너_게시글_식별자_값);
        서포터가_러너_게시글의_리뷰를_완료로_변경하는_것을_성공한다(서포터_에단_액세스_토큰, 디투_러너_게시글_식별자_값);

        // when
        final RunnerPostResponse.Count 기대된_러너_게시글_개수 = 러너_게시글_개수_응답(1);

        // then
        RunnerPostCountBySupporterSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_에단_액세스_토큰)
                .리뷰_상태로_로그인한_서포터와_연관된_러너_게시글_개수를_조회한다(ReviewStatus.DONE)

                .서버_응답()
                .서포터와_연관된_러너_게시글_개수_조회_성공을_검증한다(기대된_러너_게시글_개수);
    }

    @Test
    void 타인이_서포터가_완료한_러너_게시글_개수_조회에_성공한다() {
        // given
        final String 러너_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ditooAuthCode());
        final String 서포터_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ethanAuthCode());
        final SocialId 서포터_소셜_아이디 = jwtTestManager.parseToSocialId(서포터_액세스_토큰);
        final Supporter 서포터 = supporterRepository.getBySocialId(서포터_소셜_아이디);

        final Long 러너_게시글_식별자_값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(러너_액세스_토큰);

        서포터가_러너_게시글에_리뷰_신청을_성공한다(서포터_액세스_토큰, 러너_게시글_식별자_값);
        러너가_서포터의_리뷰_신청_선택에_성공한다(서포터, 러너_액세스_토큰, 러너_게시글_식별자_값);
        서포터가_러너_게시글의_리뷰를_완료로_변경하는_것을_성공한다(서포터_액세스_토큰, 러너_게시글_식별자_값);

        // when
        final RunnerPostResponse.Count 기대된_러너_게시글_개수 = 러너_게시글_개수_응답(1);

        // then
        RunnerPostCountBySupporterSupport
                .클라이언트_요청()
                .서포터_식별자_값으로_서포터가_완료한_러너_게시글_개수를_조회한다(서포터.getId())

                .서버_응답()
                .서포터와_연관된_러너_게시글_개수_조회_성공을_검증한다(기대된_러너_게시글_개수);
    }

    private Long 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(final String 러너_액세스_토큰) {
        return RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(러너_액세스_토큰)
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
        클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_액세스_토큰)
                .서포터가_러너_게시글에_리뷰를_신청한다(러너_게시글_식별자값, "안녕하세요. 서포터 헤나입니다.")

                .서버_응답()
                .서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(러너_게시글_식별자값);
    }

    private void 러너가_서포터의_리뷰_신청_선택에_성공한다(final Supporter 서포터, final String 러너_액세스_토큰, final Long 러너_게시글_식별자값) {
        RunnerPostUpdateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(러너_액세스_토큰)
                .러너가_서포터를_선택한다(러너_게시글_식별자값, 러너의_서포터_선택_요청(서포터.getId()))

                .서버_응답()
                .러너_게시글에_서포터가_성공적으로_선택되었는지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }

    private void 서포터가_러너_게시글의_리뷰를_완료로_변경하는_것을_성공한다(final String 서포터_액세스_토큰, final Long 러너_게시글_식별자값) {
        RunnerPostUpdateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(서포터_액세스_토큰)
                .서포터가_리뷰를_완료하고_리뷰완료_버튼을_누른다(러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글이_성공적으로_리뷰_완료_상태인지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }
}
