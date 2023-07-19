package touch.baton.assure.runnerpost;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class RunnerPostAssuredSupport {

    private RunnerPostAssuredSupport() {
    }

    public static RunnerPostClientRequestBuilder 클라이언트_요청() {
        return new RunnerPostClientRequestBuilder();
    }

    public static class RunnerPostClientRequestBuilder {

        private ExtractableResponse<Response> response;

        public RunnerPostClientRequestBuilder 러너_게시글_식별자값으로_러너_게시글을_조회한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.get("/api/v1/posts/runner/{runnerPostId}", "runnerPostId", 러너_게시글_식별자값);
            return this;
        }

        public RunnerPostClientRequestBuilder 러너_게시글_식별자값으로_러너_게시글을_삭제한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.delete("/api/v1/posts/runner/{runnerPostId}", "runnerPostId", 러너_게시글_식별자값);
            return this;
        }

        public RunnerPostServerResponseBuilder 서버_응답() {
            return new RunnerPostServerResponseBuilder(response);
        }
    }

    public static class RunnerPostServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 러너_게시글_단건_조회_성공을_검증한다(final RunnerPostResponse.SingleRunnerPost 러너_게시글_응답) {
            final RunnerPostResponse.SingleRunnerPost actual = this.response.as(new TypeRef<>() {});

            assertThat(actual)
                    .usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(러너_게시글_응답);
        }

        public void 러너_게시글_삭제_성공을_검증한다(final HttpStatus HTTP_STATUS) {
            assertThat(response.statusCode())
                    .isEqualTo(HTTP_STATUS.value());
        }
    }
}
