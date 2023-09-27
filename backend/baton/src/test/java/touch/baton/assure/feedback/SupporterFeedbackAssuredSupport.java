package touch.baton.assure.feedback;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.tobe.domain.feedback.command.service.dto.SupporterFeedBackCreateRequest;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class SupporterFeedbackAssuredSupport {

    private SupporterFeedbackAssuredSupport() {
    }

    public static SupporterFeedbackClientRequestBuilder 클라이언트_요청() {
        return new SupporterFeedbackClientRequestBuilder();
    }

    public static SupporterFeedBackCreateRequest 서포터_피드백_요청(final String 리뷰_타입,
                                                            final List<String> 디스크립션,
                                                            final Long 서포터_식별자값,
                                                            final Long 러너_게시글_식별자값
    ) {
        return new SupporterFeedBackCreateRequest(리뷰_타입, 디스크립션, 서포터_식별자값, 러너_게시글_식별자값);
    }

    public static class SupporterFeedbackClientRequestBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public SupporterFeedbackClientRequestBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public SupporterFeedbackClientRequestBuilder 서포터_피드백을_등록한다(final SupporterFeedBackCreateRequest 서포터_피드백_정보) {
            response = AssuredSupport.post("/api/v1/feedback/supporter", accessToken, 서포터_피드백_정보);
            return this;
        }

        public SupporterFeedbackServerResponseBuilder 서버_응답() {
            return new SupporterFeedbackServerResponseBuilder(response);
        }
    }

    public static class SupporterFeedbackServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public SupporterFeedbackServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 서포터_피드백_등록_성공을_검증한다(final HttpStatusAndLocationHeader 응답상태_및_로케이션) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(응답상태_및_로케이션.getHttpStatus().value());
                softly.assertThat(response.header(HttpHeaders.LOCATION)).contains(응답상태_및_로케이션.getLocation());
            });
        }
    }
}
