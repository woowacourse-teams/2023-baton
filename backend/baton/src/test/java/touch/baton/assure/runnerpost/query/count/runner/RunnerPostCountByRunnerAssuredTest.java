package touch.baton.assure.runnerpost.query.count.runner;

import org.junit.jupiter.api.Test;
import touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport;
import touch.baton.assure.runnerpost.support.query.count.runner.RunnerPostCountByRunnerSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.FakeAuthCodes;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.support.query.count.runner.RunnerPostCountByRunnerSupport.러너_게시글_개수_응답;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostCountByRunnerAssuredTest extends AssuredTestConfig {

    @Test
    void 로그인한_러너와_연관된_러너_게시글_개수_조회에_성공한다() {
        // given
        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ditooAuthCode());
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());
        러너_게시글_생성에_성공한다(디투_액세스_토큰);
        러너_게시글_생성에_성공한다(헤나_액세스_토큰);

        // when
        final RunnerPostResponse.Count 기대된_러너_게시글_개수 = 러너_게시글_개수_응답(1);

        // then
        RunnerPostCountByRunnerSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(디투_액세스_토큰)
                .리뷰_상태로_로그인한_러너와_연관된_러너_게시글_개수를_조회한다(ReviewStatus.NOT_STARTED)

                .서버_응답()
                .로그인한_러너와_연관된_러너_게시글_개수_조회_성공을_검증한다(기대된_러너_게시글_개수);
    }

    private void 러너_게시글_생성에_성공한다(final String 액세스_토큰) {
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
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
                .러너_게시글_생성_성공을_검증한다();
    }
}
