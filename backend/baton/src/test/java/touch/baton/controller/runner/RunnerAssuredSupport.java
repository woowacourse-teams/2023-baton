package touch.baton.controller.runner;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.runner.controller.response.RunnerMyProfileResponse;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerAssuredSupport {

    private RunnerAssuredSupport() {
    }

    public static RunnerClientRequestBuilder 클라이언트_요청() {
        return new RunnerClientRequestBuilder();
    }

    public static class RunnerClientRequestBuilder {

        private ExtractableResponse<Response> response;

        public RunnerClientRequestBuilder 러너_액세스_토큰으로_러너_프로필을_조회한다(final String 러너_액세스_토큰) {
            response = AssuredSupport.get("/api/v1/profile/runner", 러너_액세스_토큰);
            return this;
        }

        public RunnerServerResponseBuilder 서버_응답() {
            return new RunnerServerResponseBuilder(response);
        }
    }

    public static class RunnerServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 러너_본인_프로필_조회_성공을_검증한다(final RunnerMyProfileResponse 러너_프로필_응답) {
            final RunnerMyProfileResponse actual = this.response.as(RunnerMyProfileResponse.class);

            assertSoftly(softly -> {
                        softly.assertThat(actual.profile().name()).isEqualTo(러너_프로필_응답.profile().name());
                        softly.assertThat(actual.profile().imageUrl()).isEqualTo(러너_프로필_응답.profile().imageUrl());
                        softly.assertThat(actual.profile().githubUrl()).isEqualTo(러너_프로필_응답.profile().githubUrl());
                        softly.assertThat(actual.profile().introduction()).isEqualTo(러너_프로필_응답.profile().introduction());
                        softly.assertThat(actual.runnerPosts().size()).isEqualTo(러너_프로필_응답.runnerPosts().size());
                        softly.assertThat(actual.runnerPosts().get(0).runnerPostId()).isEqualTo(러너_프로필_응답.runnerPosts().get(0).runnerPostId());
                        softly.assertThat(actual.runnerPosts().get(0).title()).isEqualTo(러너_프로필_응답.runnerPosts().get(0).title());
                        softly.assertThat(actual.runnerPosts().get(0).deadline()).isEqualToIgnoringSeconds(러너_프로필_응답.runnerPosts().get(0).deadline());
                        softly.assertThat(actual.runnerPosts().get(0).tags()).isEqualTo(러너_프로필_응답.runnerPosts().get(0).tags());
                        softly.assertThat(actual.runnerPosts().get(0).reviewStatus()).isEqualTo(러너_프로필_응답.runnerPosts().get(0).reviewStatus());
                    }
            );
        }
    }
}
