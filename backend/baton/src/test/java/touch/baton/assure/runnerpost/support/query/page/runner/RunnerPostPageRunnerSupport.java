package touch.baton.assure.runnerpost.support.query.page.runner;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostPageRunnerSupport {

    private RunnerPostPageRunnerSupport() {
    }

    public static RunnerPostPageRunnerBuilder 클라이언트_요청() {
        return new RunnerPostPageRunnerBuilder();
    }


    public static RunnerPostResponse.SimpleByRunner 러너와_연관된_러너_게시글_응답(final RunnerPost 러너_게시글,
                                                                      final Long 지원한_서포터_식별자값,
                                                                      final int 조회수,
                                                                      final long 지원한_서포터_수,
                                                                      final ReviewStatus 리뷰_상태,
                                                                      final List<String> 러너_게시글_태그_목록
    ) {
        return new RunnerPostResponse.SimpleByRunner(
                러너_게시글.getId(),
                러너_게시글.getTitle().getValue(),
                러너_게시글.getDeadline().getValue(),
                조회수,
                지원한_서포터_수,
                리뷰_상태.name(),
                러너_게시글.getIsReviewed().getValue(),
                지원한_서포터_식별자값,
                러너_게시글_태그_목록
        );
    }

    public static PageResponse<RunnerPostResponse.SimpleByRunner> 러너와_연관된_러너_게시글_페이징_응답(
            final List<RunnerPostResponse.SimpleByRunner> 러너_게시글_목록,
            final PageResponse.PageInfo 페이징_정보
    ) {
        return new PageResponse<>(러너_게시글_목록, 페이징_정보);
    }

    public static class RunnerPostPageRunnerBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostPageRunnerBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostPageRunnerBuilder 러너와_연관된_러너_게시글_첫_페이지를_조회한다(final ReviewStatus 리뷰_상태, final int 페이지_크기) {
            final Map<String, Object> queryParams = Map.of(
                    "reviewStatus", 리뷰_상태,
                    "limit", 페이지_크기
            );

            response = AssuredSupport.get("/api/v1/posts/runner/me/runner", accessToken, new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageRunnerBuilder 러너와_연관된_러너_게시글_중간_페이지를_조회한다(final Long 이전_페이지_마지막_게시글_식별자값,
                                                                       final ReviewStatus 리뷰_상태,
                                                                       final int 페이지_크기
        ) {
            final Map<String, Object> queryParams = Map.of(
                    "cursor", 이전_페이지_마지막_게시글_식별자값,
                    "reviewStatus", 리뷰_상태,
                    "limit", 페이지_크기
            );

            response = AssuredSupport.get("/api/v1/posts/runner/me/runner", accessToken, new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageRunnerResponseBuilder 서버_응답() {
            return new RunnerPostPageRunnerResponseBuilder(response);
        }
    }

    public static class RunnerPostPageRunnerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostPageRunnerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 러너와_연관된_러너_게시글_페이징_조회_성공을_검증한다(
                final PageResponse<RunnerPostResponse.SimpleByRunner> 마이페이지_러너_게시글_페이징_응답
        ) {
            final PageResponse<RunnerPostResponse.SimpleByRunner> actual = this.response.as(new TypeRef<>() {
            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual.data()).isEqualTo(마이페이지_러너_게시글_페이징_응답.data());
                    }
            );
        }
    }
}
