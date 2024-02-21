package touch.baton.assure.runnerpost.support.query.count;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostStatusCountAssuredSupport {

    private RunnerPostStatusCountAssuredSupport() {
    }

    public static RunnerPostStatusCountBuilder 클라이언트_요청() {
        return new RunnerPostStatusCountBuilder();
    }

    public static class RunnerPostStatusCountBuilder {

        private ExtractableResponse<Response> response;

        public RunnerPostStatusCountBuilder 게시글의_상태별로_게시글_총_수를_반환한다() {
            response = AssuredSupport.get("/api/v1/posts/runner/count");
            return this;
        }

        public RunnerPostStatusCountResponseBuilder 서버_응답() {
            return new RunnerPostStatusCountResponseBuilder(response);
        }

        public class RunnerPostStatusCountResponseBuilder {

            private final ExtractableResponse<Response> response;

            public RunnerPostStatusCountResponseBuilder(final ExtractableResponse<Response> 응답) {
                this.response = 응답;
            }

            public void 게시글_상태별_게시글_총_수_반환_성공을_확인한다(final Long 리뷰_대기중_글_개수,
                                                    final Long 리뷰_진행중_글_개수,
                                                    final Long 리뷰_완료_글_개수,
                                                    final Long 기간_만료_글_개수
            ) {
                final RunnerPostResponse.StatusCount actual = response.as(RunnerPostResponse.StatusCount.class);

                assertAll(
                        () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                        () -> assertThat(actual.notStarted()).isEqualTo(리뷰_대기중_글_개수),
                        () -> assertThat(actual.inProgress()).isEqualTo(리뷰_진행중_글_개수),
                        () -> assertThat(actual.done()).isEqualTo(리뷰_완료_글_개수),
                        () -> assertThat(actual.overdue()).isEqualTo(기간_만료_글_개수)
                );
            }
        }
    }

}
