package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.common.response.ErrorResponse;
import touch.baton.domain.member.Member;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostCreateAssuredTest extends AssuredTestConfig {

    private static String 액세스_토큰;

    @BeforeEach
    void setUp() {
        final String 소셜_아이디 = "hongSile";
        final Member 사용자 = memberRepository.save(MemberFixture.createWithSocialId(소셜_아이디));
        runnerRepository.save(RunnerFixture.createRunner(사용자));
        액세스_토큰 = login(소셜_아이디);
    }

    @Test
    void 러너_게시글_등록이_성공한다() {
        // given
        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "싸게 부탁드려요."
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_성공을_검증한다(new HttpStatusAndLocationHeader(CREATED, "/api/v1/posts/runner"));
    }

    @Test
    void 게시글_제목이_null이면_러너_게시글_등록_실패한다() {
        // given
        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest(null,
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "싸게 부탁드려요."
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP001", "제목을 입력해주세요."));
    }

    @Test
    void 게시글_태그가_null이면_러너_게시글_등록_실패한다() {
        // given
        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                null,
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "싸게 부탁드려요."
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP008", "태그 목록을 빈 값이라도 입력해주세요."));
    }

    @Test
    void 게시글_PR_URL이_null이면_러너_게시글_등록_실패한다() {
        // given
        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                null,
                LocalDateTime.now().plusDays(10),
                "싸게 부탁드려요."
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP002", "PR 주소를 입력해주세요."));
    }

    @Test
    void 게시글_마감기한이_null이면_러너_게시글_등록_실패한다() {
        // given
        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                null,
                "싸게 부탁드려요."
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP003", "마감일을 입력해주세요."));
    }

    @Test
    void 게시글_마감기한이_현재보다_과거면_러너_게시글_등록_실패한다() {
        // given
        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().minusDays(1),
                "싸게 부탁드려요."
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP006", "마감일은 오늘보다 과거일 수 없습니다."));
    }

    @Test
    void 게시글_내용이_null이면_러너_게시글_등록_실패한다() {
        // given
        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                null
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP004", "내용을 입력해주세요."));
    }

    @Test
    void 게시글_내용이_1000자_보다_길면_러너_게시글_등록_실패한다() {
        // given
        final RunnerPostCreateRequest 게시글_생성_요청 = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "12345".repeat(200) + "1"
        );

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(액세스_토큰)
                .러너_게시글_등록_요청한다(게시글_생성_요청)

                .서버_응답()
                .러너_게시글_등록_실패를_검증한다(new ErrorResponse("RP005", "내용은 1000자 까지 입력해주세요."));
    }
}
