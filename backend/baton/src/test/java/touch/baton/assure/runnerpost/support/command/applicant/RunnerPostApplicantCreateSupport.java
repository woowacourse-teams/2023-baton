package touch.baton.assure.runnerpost.support.command.applicant;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostUpdateRequest;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostApplicantCreateSupport {

    private RunnerPostApplicantCreateSupport() {
    }

    public static RunnerPostApplicantCreateBuilder 클라이언트_요청() {
        return new RunnerPostApplicantCreateBuilder();
    }

    public static RunnerPostUpdateRequest.SelectSupporter 러너의_서포터_선택_요청(final Long 러너가_선택한_서포터_식별자값) {
        return new RunnerPostUpdateRequest.SelectSupporter(러너가_선택한_서포터_식별자값);
    }

    public static class RunnerPostApplicantCreateBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostApplicantCreateBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostApplicantCreateBuilder 서포터가_러너_게시글에_리뷰를_신청한다(final Long 러너_게시글_식별자값, final String 리뷰_지원_메시지) {
            response = AssuredSupport.post("/api/v1/posts/runner/{runnerPostId}/application",
                    accessToken,
                    new PathParams(Map.of("runnerPostId", 러너_게시글_식별자값)),
                    new RunnerPostApplicantCreateRequest(리뷰_지원_메시지)
            );
            return this;
        }

        public RunnerPostApplicantCreateResponseBuilder 서버_응답() {
            return new RunnerPostApplicantCreateResponseBuilder(response);
        }
    }

    public static class RunnerPostApplicantCreateResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostApplicantCreateResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public RunnerPostApplicantCreateResponseBuilder 러너_게시글_생성_성공을_검증한다() {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                softly.assertThat(response.header(LOCATION)).startsWith("/api/v1/posts/runner/");
            });

            return this;
        }

        public Long 생성한_러너_게시글의_식별자값을_반환한다() {
            final String savedRunnerPostId = this.response.header(LOCATION).replaceFirst("/api/v1/posts/runner/", "");

            return Long.parseLong(savedRunnerPostId);
        }

        public RunnerPostApplicantCreateResponseBuilder 서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(final Long 러너_게시글_식별자값) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                softly.assertThat(response.header(LOCATION)).startsWith("/api/v1/posts/runner/" + 러너_게시글_식별자값);
            });

            return this;
        }
    }
}
