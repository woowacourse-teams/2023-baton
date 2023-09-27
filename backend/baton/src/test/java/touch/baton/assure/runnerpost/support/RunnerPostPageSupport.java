package touch.baton.assure.runnerpost.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.member.query.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.command.controller.response.RunnerPostResponses;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostPageSupport {
    private RunnerPostPageSupport() {
    }

    public static RunnerPostPageBuilder 클라이언트_요청() {
        return new RunnerPostPageBuilder();
    }

    public static RunnerPostResponse.Simple 러너_게시글_Simple_응답(final RunnerPost 러너_게시글,
                                                             final int 조회수,
                                                             final long 지원한_서포터_수,
                                                             final ReviewStatus 리뷰_상태,
                                                             final List<String> 러너_게시글_태그_목록
    ) {
        return new RunnerPostResponse.Simple(
                러너_게시글.getId(),
                러너_게시글.getTitle().getValue(),
                러너_게시글.getDeadline().getValue(),
                조회수,
                지원한_서포터_수,
                리뷰_상태.name(),
                RunnerResponse.Simple.from(러너_게시글.getRunner()),
                러너_게시글_태그_목록
        );
    }

    public static RunnerPostResponses.Simple 러너_게시글_전체_Simple_페이징_응답(final List<RunnerPostResponse.Simple> 러너_게시글_목록) {
        return RunnerPostResponses.Simple.from(러너_게시글_목록);
    }

    public static class RunnerPostPageBuilder {


        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostPageBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostPageBuilder 리뷰_상태로_러너_게시글_첫_페이지를_조회한다(final int 페이지_크기, final ReviewStatus 리뷰_상태) {
            final Map<String, Object> queryParams = Map.of(
                    "limit", 페이지_크기,
                    "reviewStatus", 리뷰_상태
            );

            response = AssuredSupport.get("/api/v1/posts/runner", new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageBuilder 리뷰_상태로_러너_게시글_중간_페이지를_조회한다(
                final Long 이전_페이지_마지막_게시글_식별자값,
                final int 페이지_크기,
                final ReviewStatus 리뷰_상태
        ) {
            final Map<String, Object> queryParams = Map.of(
                    "cursor", 이전_페이지_마지막_게시글_식별자값,
                    "limit", 페이지_크기,
                    "reviewStatus", 리뷰_상태
            );

            response = AssuredSupport.get("/api/v1/posts/runner", new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageBuilder 태그_이름과_리뷰_상태를_조건으로_러너_게시글_첫_페이지를_조회한다(final int 페이지_크기, final String 태그_이름, final ReviewStatus 리뷰_상태) {
            final Map<String, Object> queryParams = Map.of(
                    "limit", 페이지_크기,
                    "tagName", 태그_이름,
                    "reviewStatus", 리뷰_상태
            );

            response = AssuredSupport.get("/api/v1/posts/runner", new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageBuilder 태그_이름과_리뷰_상태를_조건으로_러너_게시글_중간_페이지를_조회한다(
                final Long 이전_페이지_마지막_게시글_식별자값,
                final int 페이지_크기,
                final String 태그_이름,
                final ReviewStatus 리뷰_상태
        ) {
            final Map<String, Object> queryParams = Map.of(
                    "cursor", 이전_페이지_마지막_게시글_식별자값,
                    "limit", 페이지_크기,
                    "tagName", 태그_이름,
                    "reviewStatus", 리뷰_상태
            );

            response = AssuredSupport.get("/api/v1/posts/runner", new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageResponseBuilder 서버_응답() {
            return new RunnerPostPageResponseBuilder(response);
        }

    }

    public static class RunnerPostPageResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostPageResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 리뷰_상태를_조건으로_러너_게시글_페이징_조회_성공을_검증한다(final RunnerPostResponses.Simple 러너_게시글_페이징_응답) {
            final RunnerPostResponses.Simple actual = this.response.as(RunnerPostResponses.Simple.class);

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual).isEqualTo(러너_게시글_페이징_응답);
                    }
            );
        }

        public void 태그_이름과_리뷰_상태를_조건으로_러너_게시글_페이징_조회_성공을_검증한다(final RunnerPostResponses.Simple 러너_게시글_페이징_응답) {
            final RunnerPostResponses.Simple actual = this.response.as(RunnerPostResponses.Simple.class);

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual).isEqualTo(러너_게시글_페이징_응답);
                    }
            );
        }
    }
}
