package touch.baton.assure.runnerpost.support.query.count.supporter;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostCountBySupporterSupport {

    private RunnerPostCountBySupporterSupport() {
    }

    public static RunnerPostCountBySupporterBuilder 클라이언트_요청() {
        return new RunnerPostCountBySupporterBuilder();
    }

    public static RunnerPostResponse.Count 러너_게시글_개수_응답(final long 게시글_개수) {
        return new RunnerPostResponse.Count(게시글_개수);
    }

    public static class RunnerPostCountBySupporterBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostCountBySupporterBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostCountBySupporterBuilder 리뷰_상태로_로그인한_서포터와_연관된_러너_게시글_개수를_조회한다(final ReviewStatus 리뷰_상태) {
            final Map<String, Object> queryParams = Map.of("reviewStatus", 리뷰_상태);

            response = AssuredSupport.get("/api/v1/posts/runner/me/supporter/count", accessToken, new QueryParams(queryParams));
            return this;
        }

        public RunnerPostCountBySupporterBuilder 서포터_식별자_값으로_서포터가_완료한_러너_게시글_개수를_조회한다(final Long 서포터_식별자_값) {
            final Map<String, Object> queryParams = Map.of(
                    "reviewStatus", ReviewStatus.DONE,
                    "supporterId", 서포터_식별자_값
            );

            response = AssuredSupport.get("/api/v1/posts/runner/search/count", new QueryParams(queryParams));
            return this;
        }

        public RunnerPostCountBySupporterResponseBuilder 서버_응답() {
            return new RunnerPostCountBySupporterResponseBuilder(response);
        }
    }

    public static class RunnerPostCountBySupporterResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostCountBySupporterResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 서포터와_연관된_러너_게시글_개수_조회_성공을_검증한다(final RunnerPostResponse.Count 러너_게시글_개수_응답) {
            final RunnerPostResponse.Count actual = this.response.as(RunnerPostResponse.Count.class);
            assertSoftly(softly -> {
                softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(actual).isEqualTo(러너_게시글_개수_응답);
            });
        }
    }
}
