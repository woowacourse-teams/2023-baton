package touch.baton.assure.runnerpost;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class RunnerPostAssuredCreateSupport {

    private RunnerPostAssuredCreateSupport() {
    }

    public static RunnerPostClientRequestBuilder 클라이언트_요청() {
        return new RunnerPostClientRequestBuilder();
    }

    public static RunnerPostCreateRequest 러너_게시글_생성_요청(final String 러너_게시글_제목,
                                                       final List<String> 태그_목록,
                                                       final String 풀_리퀘스트,
                                                       final LocalDateTime 마감기한,
                                                       final String 러너_게시글_내용
    ) {
        return new RunnerPostCreateRequest(러너_게시글_제목, 태그_목록, 풀_리퀘스트, 마감기한, 러너_게시글_내용);
    }

    public static class RunnerPostClientRequestBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostClientRequestBuilder 토큰으로_로그인한다(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public RunnerPostClientRequestBuilder 러너가_러너_게시글을_작성한다(final RunnerPostCreateRequest request) {
            response = AssuredSupport.post("/api/v1/posts", accessToken, request);
            return this;
        }

        public RunnerPostClientRequestBuilder 서포터가_러너_게시글에_리뷰를_신청한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.post("/api/v1/posts/{runnerPostId}/applicant", accessToken, Map.of("runnerPostId", 러너_게시글_식별자값));
            return this;
        }

        public RunnerPostServerResponseBuilder 서버_응답() {
            return new RunnerPostServerResponseBuilder(response);
        }
    }

    public static class RunnerPostServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public RunnerPostServerResponseBuilder 러너_게시글_생성_성공을_검증한다() {
            final ResponseEntity<Void> actual = this.response.as(ResponseEntity.class);

            assertSoftly(softly -> {
                softly.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
            });

            return this;
        }

        public RunnerPostServerResponseBuilder 서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(final Long 러너_게시글_식별자값) {
            final ResponseEntity<Void> actual = this.response.as(ResponseEntity.class);

            assertSoftly(softly -> {
                softly.assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
                softly.assertThat(actual.getHeaders().getLocation().getPath()).isEqualTo("/api/v1/posts/runner/" + 러너_게시글_식별자값);
            });

            return this;
        }

        public Long 생성한_러너_게시글의_식별자값을_반환한다() {
            final ResponseEntity responseEntity = this.response.as(ResponseEntity.class);
            final String savedRunnerPostId = responseEntity.getHeaders().getLocation().getPath().replaceFirst("/api/v1/posts/", "");

            return Long.parseLong(savedRunnerPostId);
        }
    }
}
