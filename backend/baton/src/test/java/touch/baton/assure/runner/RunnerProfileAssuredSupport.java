package touch.baton.assure.runner;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.runner.controller.response.RunnerProfileResponse;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class RunnerProfileAssuredSupport {

    public static RunnerProfileClientRequestBuilder 클라이언트_요청() {
        return new RunnerProfileClientRequestBuilder();
    }

    public static class RunnerProfileClientRequestBuilder {

        private ExtractableResponse<Response> response;

        public RunnerProfileClientRequestBuilder 러너_피드백을_상세_조회한다(final Long runnerId) {
            response = AssuredSupport.get("/api/v1/profile/runner/{runnerId}", "runnerId", runnerId);
            return this;
        }

        public RunnerProfileServerResponseBuilder 서버_응답() {
            return new RunnerProfileServerResponseBuilder(response);
        }
    }

    public static class RunnerProfileServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerProfileServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 러너_프로필_상세_조회를_검증한다(RunnerProfileResponse.Detail 러너_프로필_상세_응답) {
            final RunnerProfileResponse.Detail actual = this.response.as(RunnerProfileResponse.Detail.class);
            assertSoftly(softly -> {
                        softly.assertThat(actual.runnerId()).isNotNull();
                        softly.assertThat(actual.name()).isEqualTo(러너_프로필_상세_응답.name());
                        softly.assertThat(actual.imageUrl()).isEqualTo(러너_프로필_상세_응답.imageUrl());
                        softly.assertThat(actual.githubUrl()).isEqualTo(러너_프로필_상세_응답.githubUrl());
                        softly.assertThat(actual.introduction()).isEqualTo(러너_프로필_상세_응답.introduction());
                        softly.assertThat(actual.company()).isEqualTo(러너_프로필_상세_응답.company());
                        softly.assertThat(actual.technicalTags()).containsExactlyElementsOf(러너_프로필_상세_응답.technicalTags());
                    }
            );
        }
    }
}
