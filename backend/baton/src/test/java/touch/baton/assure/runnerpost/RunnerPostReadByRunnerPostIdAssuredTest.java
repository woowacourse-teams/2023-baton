package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.runnerpost.command.controller.response.RunnerPostResponse;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.RunnerPostAssuredCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.러너_게시글_Detail_응답;
import static touch.baton.fixture.vo.WatchedCountFixture.watchedCount;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostReadByRunnerPostIdAssuredTest extends AssuredTestConfig {

    @Test
    void 러너의_게시글_식별자값으로_러너_게시글_상세_정보_조회에_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Runner 러너_헤나 = runnerRepository.getBySocialId(헤나_소셜_아이디);

        final Long 헤나_러너_게시글_식별자값 = 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환에_성공한다(헤나_액세스_토큰);
        final RunnerPost 헤나_러너_게시글 = runnerPostRepository.getByRunnerPostId(헤나_러너_게시글_식별자값);

        final RunnerPostResponse.Detail 러너_게시글_detail_응답 = 러너_게시글_Detail_응답을_생성한다(
                러너_헤나, 헤나_러너_게시글,
                watchedCount(1).getValue(),
                0,
                false,
                List.of("자바", "스프링")
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_조회한다(헤나_러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글_단건_조회_성공을_검증한다(러너_게시글_detail_응답);
    }

    private Long 러너_게시글_생성을_성공하고_러너_게시글_식별자값을_반환에_성공한다(final String 헤나_액세스_토큰) {
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

    private RunnerPostResponse.Detail 러너_게시글_Detail_응답을_생성한다(final Runner 작성자_러너,
                                                             final RunnerPost 러너_게시글,
                                                             final int 조회수,
                                                             final long 서포터_지원자수,
                                                             final boolean 서포터_지원_여부,
                                                             final List<String> 러너_게시글_태그_목록
    ) {
        return 러너_게시글_Detail_응답(
                러너_게시글.getId(),
                러너_게시글.getTitle().getValue(),
                러너_게시글.getImplementedContents().getValue(),
                러너_게시글.getCuriousContents().getValue(),
                러너_게시글.getPostscriptContents().getValue(),
                러너_게시글.getPullRequestUrl().getValue(),
                러너_게시글.getDeadline().getValue(),
                조회수,
                서포터_지원자수,
                러너_게시글.getReviewStatus(),
                !러너_게시글.isNotOwner(작성자_러너),
                서포터_지원_여부,
                러너_게시글.getRunner(),
                러너_게시글_태그_목록
        );
    }
}
