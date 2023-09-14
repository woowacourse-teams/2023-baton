package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.RunnerPostAssuredCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.*;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostReadAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_게시글_전체_조회에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.ditooAuthCode());
        final Long 디투_러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(디투_액세스_토큰);

        final RunnerPost 디투_러너_게시글 = runnerPostRepository.getByRunnerPostId(디투_러너_게시글_식별자값);
        final Long 디투_러너_게시글에_지원한_서포터_수 = supporterRunnerPostRepository.getApplicantCountByRunnerPostId(디투_러너_게시글_식별자값);

        final PageRequest 페이징_정보 = PageRequest.of(1, 10);
        final RunnerPostResponse.Simple 디투_러너_게시글_Simple_응답 = 러너_게시글_Simple_응답(디투_러너_게시글, 0, 디투_러너_게시글에_지원한_서포터_수, ReviewStatus.NOT_STARTED, List.of("자바", "스프링"));

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .리뷰_상태로_전체_러너_게시글_페이징을_조회한다(페이징_정보, ReviewStatus.NOT_STARTED)

                .서버_응답()
                .전체_러너_게시글_페이징_조회_성공을_검증한다(
                        러너_게시글_전체_Simple_페이징_응답(페이징_정보, List.of(디투_러너_게시글_Simple_응답))
                );
    }

    @Test
    void 마이페이지_러너_게시글_페이징_조회에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());
        final Long 헤나_러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(헤나_액세스_토큰);

        final RunnerPost 헤나_러너_게시글 = runnerPostRepository.getByRunnerPostId(헤나_러너_게시글_식별자값);
        final Long 헤나_러너_게시글에_지원한_서포터_수 = supporterRunnerPostRepository.getApplicantCountByRunnerPostId(헤나_러너_게시글_식별자값);

        final PageRequest 페이징_정보 = PageRequest.of(1, 10);

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .마이페이지_러너_게시글_페이징을_조회한다(ReviewStatus.NOT_STARTED, 페이징_정보)

                .서버_응답()
                .마이페이지_러너_게시글_페이징_조회_성공을_검증한다(
                        마이페이지_러너_게시글_응답(페이징_정보,
                                List.of(
                                        마이페이지_러너_게시글_SimpleInMyPage_응답(헤나_러너_게시글, null, 0, 헤나_러너_게시글에_지원한_서포터_수, ReviewStatus.NOT_STARTED, List.of("자바", "스프링")
                                        )
                                ))
                );
    }

    private Long 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환한다(final String 사용자_액세스_토큰) {
        return RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(사용자_액세스_토큰)
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
}
