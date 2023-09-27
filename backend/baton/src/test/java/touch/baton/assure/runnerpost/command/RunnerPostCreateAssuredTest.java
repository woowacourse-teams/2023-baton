package touch.baton.assure.runnerpost.command;

import org.junit.jupiter.api.Test;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.runnerpost.support.command.RunnerPostCreateSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.domain.common.response.ErrorResponse;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostCreateAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_게시글_등록이_성공한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "싸게 부탁드려요.",
                "이거 궁금해요.",
                "잘 부탁드립니다."
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_성공을_검증한다(new HttpStatusAndLocationHeader(CREATED, "/api/v1/posts/runner"));
    }

    @Test
    void 게시글_제목이_null이면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest(null,
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "싸게 부탁드려요.",
                "이거 궁금해요.",
                "잘 부탁드립니다."
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP001", "제목을 입력해주세요."));
    }

    @Test
    void 게시글_태그가_null이면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                null,
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "싸게 부탁드려요.",
                "이거 궁금해요.",
                "잘 부탁드립니다."
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP008", "태그 목록을 빈 값이라도 입력해주세요."));
    }

    @Test
    void 게시글_PR_URL이_null이면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                null,
                LocalDateTime.now().plusDays(10),
                "싸게 부탁드려요.",
                "이거 궁금해요.",
                "잘 부탁드립니다."
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP002", "PR 주소를 입력해주세요."));
    }

    @Test
    void 게시글_마감기한이_null이면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                null,
                "싸게 부탁드려요.",
                "이거 궁금해요.",
                "잘 부탁드립니다."
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP003", "마감일을 입력해주세요."));
    }

    @Test
    void 게시글_마감기한이_현재보다_과거면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().minusDays(1),
                "싸게 부탁드려요.",
                "이거 궁금해요.",
                "잘 부탁드립니다."
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP006", "마감일은 오늘보다 과거일 수 없습니다."));
    }

    @Test
    void 구현_내용이_null이면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                null,
                "이거 궁금해요.",
                "잘 부탁드립니다."
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP004", "구현 내용을 입력해주세요."));
    }

    @Test
    void 궁금한_내용이_null이면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "이거 해줘요.",
                null,
                "잘 부탁드립니다."
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP012", "궁금한 내용을 입력해주세요."));
    }

    @Test
    void 참고_사항이_null이면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "잘 부탁드립니다.",
                "이거 궁금해요.",
                null
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP013", "참고 사항을 입력해주세요."));
    }

    @Test
    void 게시글_내용이_1000자_보다_길면_러너_게시글_등록_실패한다() {
        // given
        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(MockAuthCodes.hyenaAuthCode());

        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "12345".repeat(200) + "1",
                "",
                ""
        );

        // when, then
        RunnerPostCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP005", "내용은 1000자 까지 입력해주세요."));
    }
}
