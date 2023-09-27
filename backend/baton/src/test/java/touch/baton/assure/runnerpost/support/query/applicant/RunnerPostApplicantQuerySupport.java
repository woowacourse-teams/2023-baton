package touch.baton.assure.runnerpost.support.query.applicant;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.controller.response.SupporterRunnerPostResponse;
import touch.baton.domain.runnerpost.command.controller.response.SupporterRunnerPostResponses;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostApplicantQuerySupport {

    private RunnerPostApplicantQuerySupport() {
    }

    public static RunnerPostApplicantQueryBuilder 클라이언트_요청() {
        return new RunnerPostApplicantQueryBuilder();
    }

    public static SupporterRunnerPostResponses.Detail 지원한_서포터_목록_응답(final List<SupporterRunnerPostResponse.Detail> 지원한_서포터_응답_목록) {
        return SupporterRunnerPostResponses.Detail.from(지원한_서포터_응답_목록);
    }

    public static SupporterRunnerPostResponse.Detail 지원한_서포터_응답(final Supporter 지원한_서포터,
                                                                final int 서포터의_리뷰수,
                                                                final String 지원한_서포터_어필_메시지,
                                                                final List<String> 서포터_기술_태그_목록
    ) {
        return new SupporterRunnerPostResponse.Detail(
                지원한_서포터.getId(),
                지원한_서포터.getMember().getMemberName().getValue(),
                지원한_서포터.getMember().getCompany().getValue(),
                서포터의_리뷰수,
                지원한_서포터.getMember().getImageUrl().getValue(),
                지원한_서포터_어필_메시지,
                서포터_기술_태그_목록
        );
    }

    public static class RunnerPostApplicantQueryBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostApplicantQueryBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostApplicantQueryBuilder 러너_게시글_식별자값으로_지원한_서포터_목록을_조회한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.get(
                    "/api/v1/posts/runner/{runnerPostId}/supporters",
                    accessToken,
                    new PathParams(Map.of("runnerPostId", 러너_게시글_식별자값)));
            return this;
        }

        public RunnerPostApplicantQueryResponseBuilder 서버_응답() {
            return new RunnerPostApplicantQueryResponseBuilder(response);
        }
    }

    public static class RunnerPostApplicantQueryResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostApplicantQueryResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public RunnerPostApplicantQueryResponseBuilder 러너_게시글_생성_성공을_검증한다() {
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

        public RunnerPostApplicantQueryResponseBuilder 서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(final Long 러너_게시글_식별자값) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                softly.assertThat(response.header(LOCATION)).startsWith("/api/v1/posts/runner/" + 러너_게시글_식별자값);
            });

            return this;
        }

        public void 지원한_서포터_목록_조회_성공을_검증한다(final SupporterRunnerPostResponses.Detail 전체_러너_게시글_페이징_응답) {
            final SupporterRunnerPostResponses.Detail actual = this.response.as(new TypeRef<>() {

            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual).isEqualTo(전체_러너_게시글_페이징_응답);
                    }
            );
        }
    }
}
