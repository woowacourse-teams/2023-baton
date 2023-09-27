package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.RunnerPostAssuredCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.*;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostReadWithSupporterIdAndReviewStatusAssuredTest extends AssuredTestConfig {

    @Test
    void 서포터가_리뷰_완료한_러너_게시글_페이징_조회에_성공한다() {
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

        final PageRequest 페이징_정보 = PageRequest.of(1, 10);
        final RunnerPost 리뷰_신청을_대기중인_디투_러너_게시글 = runnerPostRepository.getByRunnerPostId(디투_러너_게시글_식별자값);
        final Long 디투_러너_게시글에_지원한_서포터_수 = supporterRunnerPostRepository.getApplicantCountByRunnerPostId(디투_러너_게시글_식별자값);

        final RunnerPostResponse.ReferencedBySupporter 서포터가_리뷰를_지원한_대기중인_러너_게시글_응답 = 서포터와_연관된_러너_게시글_응답(
                리뷰_신청을_대기중인_디투_러너_게시글,
                List.of("자바", "스프링"),
                0,
                디투_러너_게시글에_지원한_서포터_수,
                ReviewStatus.DONE
        );

        // then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .서포터와_연관된_러너_게시글_페이징을_조회한다(서포터_헤나.getId(), ReviewStatus.DONE, 페이징_정보)

                .서버_응답()
                .서포터와_연관된_러너_게시글_페이징_조회_성공을_검증한다(
                        서포터와_연관된_러너_게시글_페이징_응답(페이징_정보, List.of(서포터가_리뷰를_지원한_대기중인_러너_게시글_응답))
                );
    }

    private Long 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(final String 헤나_액세스_토큰) {
        return RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너가_러너_게시글을_작성한다(
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
        RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .서포터가_러너_게시글에_리뷰를_신청한다(디투_러너_게시글_식별자값, "안녕하세요. 서포터 헤나입니다.")

                .서버_응답()
                .서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(디투_러너_게시글_식별자값);
    }

    private void 러너가_서포터의_리뷰_신청_선택에_성공한다(final Supporter 서포터_헤나, final String 디투_액세스_토큰, final Long 디투_러너_게시글_식별자값) {
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(디투_액세스_토큰)
                .러너가_서포터를_선택한다(디투_러너_게시글_식별자값, 러너의_서포터_선택_요청(서포터_헤나.getId()))

                .서버_응답()
                .러너_게시글에_서포터가_성공적으로_선택되었는지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }

    private void 서포터가_러너_게시글의_리뷰를_완료로_변경하는_것을_성공한다(final String 헤나_액세스_토큰, final Long 디투_러너_게시글_식별자값) {
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .서포터가_리뷰를_완료하고_리뷰완료_버튼을_누른다(디투_러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글이_성공적으로_리뷰_완료_상태인지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }
}
