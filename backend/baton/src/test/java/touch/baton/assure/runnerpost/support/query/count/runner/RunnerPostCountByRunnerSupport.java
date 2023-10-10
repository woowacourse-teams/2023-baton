package touch.baton.assure.runnerpost.support.query.count.runner;

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
public class RunnerPostCountByRunnerSupport {

    private RunnerPostCountByRunnerSupport() {
    }

    public static RunnerPostCountByRunnerBuilder 클라이언트_요청() {
        return new RunnerPostCountByRunnerBuilder();
    }

    public static RunnerPostResponse.Count 러너_게시글_개수_응답(final long 게시글_개수) {
        return new RunnerPostResponse.Count(게시글_개수);
    }

    public static class RunnerPostCountByRunnerBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostCountByRunnerBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostCountByRunnerBuilder 리뷰_상태로_로그인한_러너와_연관된_러너_게시글_개수를_조회한다(final ReviewStatus 리뷰_상태) {
            final Map<String, Object> queryParams = Map.of("reviewStatus", 리뷰_상태);

            response = AssuredSupport.get("/api/v1/posts/runner/me/runner/count", accessToken, new QueryParams(queryParams));
            return this;
        }

        public RunnerPostCountByRunnerResponseBuilder 서버_응답() {
            return new RunnerPostCountByRunnerResponseBuilder(response);
        }
    }

    public static class RunnerPostCountByRunnerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostCountByRunnerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 로그인한_러너와_연관된_러너_게시글_개수_조회_성공을_검증한다(final RunnerPostResponse.Count 러너_게시글_개수_응답) {
            final RunnerPostResponse.Count actual = this.response.as(RunnerPostResponse.Count.class);
            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual).isEqualTo(러너_게시글_개수_응답);
            });
        }
    }
}
