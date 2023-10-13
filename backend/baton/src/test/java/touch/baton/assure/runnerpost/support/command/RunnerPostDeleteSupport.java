package touch.baton.assure.runnerpost.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostDeleteSupport {

    private RunnerPostDeleteSupport() {
    }

    public static RunnerPostDeleteBuilder 클라이언트_요청() {
        return new RunnerPostDeleteBuilder();
    }

    public static class RunnerPostDeleteBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostDeleteBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostDeleteBuilder 러너_게시글_식별자값으로_러너_게시글을_삭제한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.delete("/api/v1/posts/runner/{runnerPostId}", accessToken, new PathParams(Map.of("runnerPostId", 러너_게시글_식별자값)));
            return this;
        }

        public RunnerPostDeleteResponseBuilder 서버_응답() {
            return new RunnerPostDeleteResponseBuilder(response);
        }
    }

    public static class RunnerPostDeleteResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostDeleteResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 러너_게시글_삭제_성공을_검증한다(final HttpStatus HTTP_STATUS) {
            assertThat(response.statusCode()).isEqualTo(HTTP_STATUS.value());
        }

        public void 러너_게시글_삭제_실패를_검증한다(final HttpStatus HTTP_STATUS) {
            assertThat(response.statusCode()).isEqualTo(HTTP_STATUS.value());
        }
    }
}
