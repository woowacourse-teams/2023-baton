package touch.baton.assure.supporter;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.controller.response.SupporterResponse;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class SupporterProfileAssuredSupport {

    private SupporterProfileAssuredSupport() {
    }

    public static SupporterClientRequestBuilder 클라이언트_요청() {
        return new SupporterClientRequestBuilder();
    }

    public static SupporterResponse.Profile 서포터_프로필_응답(final Supporter 서포터) {
        return SupporterResponse.Profile.from(서포터);
    }

    public static class SupporterClientRequestBuilder {

        private ExtractableResponse<Response> response;

        public SupporterClientRequestBuilder 서포터_프로필을_서포터_식별자값으로_조회한다(final Long 서포터_식별자값) {
            response = AssuredSupport.get("/api/v1/profile/supporter/{supporterId}", "supporterId", 서포터_식별자값);
            return this;
        }

        public SupporterServerResponseBuilder 서버_응답() {
            return new SupporterServerResponseBuilder(response);
        }
    }

    public static class SupporterServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public SupporterServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 서포터_프로필_조회_성공을_검증한다(final SupporterResponse.Profile 서포터_프로필_응답) {
            final SupporterResponse.Profile actual = this.response.as(SupporterResponse.Profile.class);

            assertSoftly(softly -> {
                        softly.assertThat(actual.supporterId()).isEqualTo(서포터_프로필_응답.supporterId());
                        softly.assertThat(actual.name()).isEqualTo(서포터_프로필_응답.name());
                        softly.assertThat(actual.company()).isEqualTo(서포터_프로필_응답.company());
                        softly.assertThat(actual.imageUrl()).isEqualTo(서포터_프로필_응답.imageUrl());
                        softly.assertThat(actual.githubUrl()).isEqualTo(서포터_프로필_응답.githubUrl());
                        softly.assertThat(actual.introduction()).isEqualTo(서포터_프로필_응답.introduction());
                        softly.assertThat(actual.technicalTags()).isEqualTo(서포터_프로필_응답.technicalTags());
                    }
            );
        }
    }
}
