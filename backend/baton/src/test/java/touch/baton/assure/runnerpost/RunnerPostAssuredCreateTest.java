package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;

import static java.time.LocalDateTime.now;
import static touch.baton.assure.runnerpost.RunnerPostAssuredCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.러너_게시글_Detail_응답;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostAssuredCreateTest extends AssuredTestConfig {

    @Test
    void 러너가_러너_게시글을_생성하고_서포터가_러너_게시글에_리뷰를_신청한다() {
        final String 에단_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.ethanAuthCode());
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 러너_게시글_생성_요청 = 러너_게시글_생성_요청을_생성한다();
        final Long 에단의_러너_게시글_식별자값 = RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(에단_액세스_토큰)
                .러너가_러너_게시글을_작성한다(러너_게시글_생성_요청)

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다()
                .생성한_러너_게시글의_식별자값을_반환한다();

        final SocialId 에단의_소셜_아이디 = jwtTestManager.parseToSocialId(에단_액세스_토큰);
        final Runner 러너_에단 = runnerRepository.getBySocialId(에단의_소셜_아이디);

        final RunnerPostResponse.Detail 리뷰가_시작되지_않은_에단의_러너_게시글_Detail_응답 = 러너_게시글_Detail_응답을_생성한다(러너_에단, 러너_게시글_생성_요청, NOT_STARTED, 에단의_러너_게시글_식별자값, 1, 0L, false);
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(에단_액세스_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_조회한다(에단의_러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글_단건_조회_성공을_검증한다(리뷰가_시작되지_않은_에단의_러너_게시글_Detail_응답);

        RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .서포터가_러너_게시글에_리뷰를_신청한다(에단의_러너_게시글_식별자값, "안녕하세요. 서포터 헤나입니다.")

                .서버_응답()
                .서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(에단의_러너_게시글_식별자값);
    }

    private RunnerPostCreateRequest 러너_게시글_생성_요청을_생성한다() {
        return 러너_게시글_생성_요청(
                "러너 게시글 테스트 제목",
                List.of("java", "spring"),
                "https://github.com",
                now().plusHours(10),
                "러너 게시글 내용",
                "게시글 궁금한 내용",
                "참고 사항");
    }

    private RunnerPostResponse.Detail 러너_게시글_Detail_응답을_생성한다(final Runner 러너,
                                                             final RunnerPostCreateRequest 러너_게시글_생성_요청,
                                                             final ReviewStatus 리뷰_상태,
                                                             final Long 러너_게시글_식별자값,
                                                             final int 조회수,
                                                             final long 서포터_지원자수,
                                                             final boolean 서포터_지원_여부
    ) {
        return 러너_게시글_Detail_응답(
                러너_게시글_식별자값,
                러너_게시글_생성_요청.title(),
                러너_게시글_생성_요청.implementedContents(),
                러너_게시글_생성_요청.curiousContents(),
                러너_게시글_생성_요청.postscriptContents(),
                러너_게시글_생성_요청.pullRequestUrl(),
                러너_게시글_생성_요청.deadline(),
                조회수,
                서포터_지원자수,
                리뷰_상태,
                true,
                서포터_지원_여부,
                러너,
                러너_게시글_생성_요청.tags()
        );
    }
}
