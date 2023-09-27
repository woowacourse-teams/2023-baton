package touch.baton.assure.runnerpost.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.domain.common.response.ErrorResponse;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostCreateSupport {

    private RunnerPostCreateSupport() {
    }

    public static RunnerPostCreateBuilder 클라이언트_요청() {
        return new RunnerPostCreateBuilder();
    }

    public static RunnerPostCreateRequest 러너_게시글_생성_요청(final String 러너_게시글_제목,
                                                       final List<String> 태그_목록,
                                                       final String 풀_리퀘스트,
                                                       final LocalDateTime 마감기한,
                                                       final String 구현_내용,
                                                       final String 궁금한_내용,
                                                       final String 참고_사항
    ) {
        return new RunnerPostCreateRequest(러너_게시글_제목, 태그_목록, 풀_리퀘스트, 마감기한, 구현_내용, 궁금한_내용, 참고_사항);
    }

    public static class RunnerPostCreateBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostCreateBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }
        
        public RunnerPostCreateBuilder 러너_게시글_등록_요청한다(final RunnerPostCreateRequest 게시글_생성_요청) {
            response = AssuredSupport.post("/api/v1/posts/runner", accessToken, 게시글_생성_요청);
            return this;
        }

        public RunnerPostCreateResponseBuilder 서버_응답() {
            return new RunnerPostCreateResponseBuilder(response);
        }
    }

    public static class RunnerPostCreateResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostCreateResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public RunnerPostCreateResponseBuilder 러너_게시글_생성_성공을_검증한다() {
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

        public void 러너_게시글_등록_성공을_검증한다(final HttpStatusAndLocationHeader httpStatusAndLocationHeader) {
            assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(httpStatusAndLocationHeader.getHttpStatus().value());
                        softly.assertThat(response.header(LOCATION)).contains(httpStatusAndLocationHeader.getLocation());
                    }
            );
        }

        public void 러너_게시글_등록_실패를_검증한다(final ErrorResponse 예상_에러_응답) {
            final ErrorResponse 실제_에러_응답 = response.as(ErrorResponse.class);

            assertSoftly(softly -> {
                softly.assertThat(실제_에러_응답.errorCode()).isEqualTo(예상_에러_응답.errorCode());
                softly.assertThat(실제_에러_응답.message()).isEqualTo(예상_에러_응답.message());
            });
        }
    }
}
