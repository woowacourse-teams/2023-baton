package touch.baton.assure.runnerpost.support.command.applicant;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.common.PathParams;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostApplicantDeleteAssuredSupport {

    private RunnerPostApplicantDeleteAssuredSupport() {
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

        public void 서포터의_리뷰_제안_철회를_검증한다(final HttpStatusAndLocationHeader 응답상태_및_로케이션) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(응답상태_및_로케이션.getHttpStatus().value());
                softly.assertThat(response.header(LOCATION)).isEqualTo(응답상태_및_로케이션.getLocation());
            });
        }
    }
}
