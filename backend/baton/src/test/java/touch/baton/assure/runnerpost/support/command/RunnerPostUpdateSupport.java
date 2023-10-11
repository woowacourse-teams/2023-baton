package touch.baton.assure.runnerpost.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.assure.common.PathParams;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostUpdateRequest;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostUpdateSupport {

    private RunnerPostUpdateSupport() {
    }

    public static RunnerPostUpdateBuilder 클라이언트_요청() {
        return new RunnerPostUpdateBuilder();
    }


    public static class RunnerPostUpdateBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostUpdateBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostUpdateBuilder 서포터가_리뷰를_완료하고_리뷰완료_버튼을_누른다(final Long 게시글_식별자) {
            response = AssuredSupport.patch("/api/v1/posts/runner/{runnerPostId}/done", accessToken, new PathParams(Map.of("runnerPostId", 게시글_식별자)));
            return this;
        }

        public RunnerPostUpdateBuilder 러너가_서포터를_선택한다(final Long 게시글_식별자값,
                                                                                     final RunnerPostUpdateRequest.SelectSupporter 서포터_선택_요청_정보
        ) {
            response = AssuredSupport.patch("/api/v1/posts/runner/{runnerPostId}/supporters",
                    accessToken,
                    new PathParams(Map.of("runnerPostId", 게시글_식별자값)),
                    서포터_선택_요청_정보
            );
            return this;
        }

        public RunnerPostUpdateResponseBuilder 서버_응답() {
            return new RunnerPostUpdateResponseBuilder(response);
        }
    }

    public static class RunnerPostUpdateResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostUpdateResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 러너_게시글이_성공적으로_리뷰_완료_상태인지_확인한다(final HttpStatusAndLocationHeader httpStatusAndLocationHeader) {
            assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(httpStatusAndLocationHeader.getHttpStatus().value());
                        softly.assertThat(response.header(LOCATION)).contains(httpStatusAndLocationHeader.getLocation());
                    }
            );
        }

        public void 러너_게시글에_서포터가_성공적으로_선택되었는지_확인한다(final HttpStatusAndLocationHeader httpStatusAndLocationHeader) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(httpStatusAndLocationHeader.getHttpStatus().value());
                softly.assertThat(response.header(LOCATION)).contains(httpStatusAndLocationHeader.getLocation());
            });
        }
    }
}
