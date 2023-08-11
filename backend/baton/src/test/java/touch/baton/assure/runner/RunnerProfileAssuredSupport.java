package touch.baton.assure.runner;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.response.ErrorResponse;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.controller.response.RunnerProfileResponse;
import touch.baton.domain.runner.controller.response.RunnerResponse;
import touch.baton.domain.runner.service.dto.RunnerUpdateRequest;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class RunnerProfileAssuredSupport {

    private RunnerProfileAssuredSupport() {
    }

    public static RunnerProfileClientRequestBuilder 클라이언트_요청() {
        return new RunnerProfileClientRequestBuilder();
    }

    public static RunnerResponse.MyProfile 러너_본인_프로필_응답(final Runner 러너) {
        return RunnerResponse.MyProfile.from(러너);
    }

    public static class RunnerProfileClientRequestBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerProfileClientRequestBuilder 토큰으로_로그인한다(final String 토큰) {
            this.accessToken = 토큰;
            return this;
        }

        public RunnerProfileClientRequestBuilder 러너_본인_프로필을_가지고_있는_토큰으로_조회한다() {
            response = AssuredSupport.get("/api/v1/profile/runner/me", accessToken);
            return this;
        }

        public RunnerProfileClientRequestBuilder 러너_피드백을_상세_조회한다(final Long 러너_식별자) {
            response = AssuredSupport.get("/api/v1/profile/runner/{runnerId}", "runnerId", 러너_식별자);
            return this;
        }

        public RunnerProfileServerResponseBuilder 서버_응답() {
            return new RunnerProfileServerResponseBuilder(response);
        }

        public RunnerProfileClientRequestBuilder 러너_본인_프로필을_수정한다(final RunnerUpdateRequest request) {
            response = AssuredSupport.patch("/api/v1/profile/runner/me", accessToken, request);
            return this;
        }
    }

    public static class RunnerProfileServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerProfileServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public static RunnerProfileClientRequestBuilder 클라이언트_요청() {
            return new RunnerProfileClientRequestBuilder();
        }

        public void 러너_본인_프로필_조회_성공을_검증한다(final RunnerResponse.MyProfile 러너_본인_프로필_응답) {
            final RunnerResponse.MyProfile actual = this.response.as(RunnerResponse.MyProfile.class);

            assertSoftly(softly -> {
                softly.assertThat(actual.name()).isEqualTo(러너_본인_프로필_응답.name());
                softly.assertThat(actual.company()).isEqualTo(러너_본인_프로필_응답.company());
                softly.assertThat(actual.imageUrl()).isEqualTo(러너_본인_프로필_응답.imageUrl());
                softly.assertThat(actual.githubUrl()).isEqualTo(러너_본인_프로필_응답.githubUrl());
                softly.assertThat(actual.introduction()).isEqualTo(러너_본인_프로필_응답.introduction());
                softly.assertThat(actual.technicalTags()).isEqualTo(러너_본인_프로필_응답.technicalTags());
            });
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

        public void 러너_본인_프로필_수정_성공을_검증한다(final HttpStatus HTTP_STATUS) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HTTP_STATUS.value());
                softly.assertThat(response.header("Location")).isNotNull();
            });
        }

        public void 러너_본인_프로필_수정_실패를_검증한다(final ClientErrorCode clientErrorCode) {
            final ErrorResponse actual = this.response.as(ErrorResponse.class);

            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(clientErrorCode.getHttpStatus().value());
                softly.assertThat(actual.errorCode()).isEqualTo(clientErrorCode.getErrorCode());
            });
        }
    }
}
