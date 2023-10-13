package touch.baton.assure.member.support.query;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.service.dto.RunnerUpdateRequest;
import touch.baton.domain.member.query.controller.response.RunnerResponse;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerQueryAssuredSupport {

    private RunnerQueryAssuredSupport() {
    }

    public static RunnerQueryBuilder 클라이언트_요청() {
        return new RunnerQueryBuilder();
    }

    public static class RunnerQueryBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerQueryBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerQueryBuilder 러너_본인_프로필을_가지고_있는_액세스_토큰으로_조회한다() {
            response = AssuredSupport.get("/api/v1/profile/runner/me", accessToken);
            return this;
        }

        public RunnerQueryBuilder 러너_프로필을_상세_조회한다(final Long 러너_식별자) {
            response = AssuredSupport.get("/api/v1/profile/runner/{runnerId}", new PathParams(Map.of("runnerId", 러너_식별자)));
            return this;
        }

        public RunnerQueryResponseBuilder 서버_응답() {
            return new RunnerQueryResponseBuilder(response);
        }
    }

    public static class RunnerQueryResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerQueryResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public static RunnerQueryBuilder 클라이언트_요청() {
            return new RunnerQueryBuilder();
        }

        public static RunnerResponse.Detail 러너_프로필_상세_응답(final Runner 러너, final RunnerUpdateRequest 러너_본인_프로필_수정_요청) {
            return new RunnerResponse.Detail(
                    러너.getId(),
                    러너_본인_프로필_수정_요청.name(),
                    러너.getMember().getImageUrl().getValue(),
                    러너.getMember().getGithubUrl().getValue(),
                    러너_본인_프로필_수정_요청.introduction(),
                    러너_본인_프로필_수정_요청.company(),
                    러너_본인_프로필_수정_요청.technicalTags()
            );
        }

        public void 러너_본인_프로필_조회_성공을_검증한다(final RunnerResponse.Mine 러너_본인_프로필_응답) {
            final RunnerResponse.Mine actual = this.response.as(RunnerResponse.Mine.class);

            assertSoftly(softly -> {
                softly.assertThat(actual.name()).isEqualTo(러너_본인_프로필_응답.name());
                softly.assertThat(actual.company()).isEqualTo(러너_본인_프로필_응답.company());
                softly.assertThat(actual.imageUrl()).isEqualTo(러너_본인_프로필_응답.imageUrl());
                softly.assertThat(actual.githubUrl()).isEqualTo(러너_본인_프로필_응답.githubUrl());
                softly.assertThat(actual.introduction()).isEqualTo(러너_본인_프로필_응답.introduction());
                softly.assertThat(actual.technicalTags()).isEqualTo(러너_본인_프로필_응답.technicalTags());
            });
        }

        public void 러너_프로필_상세_조회를_검증한다(final RunnerResponse.Detail 러너_프로필_상세_응답) {
            final RunnerResponse.Detail actual = this.response.as(RunnerResponse.Detail.class);

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
