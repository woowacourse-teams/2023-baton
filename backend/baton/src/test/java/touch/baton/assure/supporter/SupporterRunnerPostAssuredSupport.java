package touch.baton.assure.supporter;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class SupporterRunnerPostAssuredSupport {

    private SupporterRunnerPostAssuredSupport() {
    }

    public static SupporterRunnerPostClientRequestBuilder 클라이언트_요청() {
        return new SupporterRunnerPostClientRequestBuilder();
    }

    public static class SupporterRunnerPostClientRequestBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public SupporterRunnerPostClientRequestBuilder 액세스_토큰으로_로그인_한다(final String 액세스_토큰) {
            accessToken = 액세스_토큰;
            return this;
        }

        public SupporterRunnerPostClientRequestBuilder 서포터가_리뷰_제안을_취소한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.patch("/api/v1/posts/runner/{runnerPostId}/cancelation", accessToken, new PathParams(Map.of("runnerPostId", 러너_게시글_식별자값)));
            return this;
        }

        public SupporterRunnerPostServerResponseBuilder 서버_응답() {
            return new SupporterRunnerPostServerResponseBuilder(response);
        }
    }

    public static class SupporterRunnerPostServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public SupporterRunnerPostServerResponseBuilder(final ExtractableResponse<Response> 응답) {
            this.response = 응답;
        }

        public void 서포터의_리뷰_제안_철회를_검증한다(final HttpStatus HTTP_STATUS,
                                        final String 응답_헤더_이름,
                                        final String 응답_헤더_값
        ) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HTTP_STATUS.value());
                softly.assertThat(response.header(응답_헤더_이름)).isEqualTo(응답_헤더_값);
            });
        }
    }
}
