package touch.baton.assure.runnerpost.support.query.detail;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.query.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostDetailSupport {

    private RunnerPostDetailSupport() {
    }

    public static RunnerPostDetailBuilder 클라이언트_요청() {
        return new RunnerPostDetailBuilder();
    }

    public static RunnerPostResponse.Detail 러너_게시글_Detail_응답(final Long 러너_게시글_식별자값,
                                                             final String 제목,
                                                             final String 구현_내용,
                                                             final String 궁금한_내용,
                                                             final String 참고_사항,
                                                             final String 풀_리퀘스트,
                                                             final LocalDateTime 마감기한,
                                                             final int 조회수,
                                                             final long 서포터_지원자수,
                                                             final ReviewStatus 리뷰_상태,
                                                             final boolean 주인_여부,
                                                             final boolean 서포터_지원_여부,
                                                             final Runner 러너,
                                                             final List<String> 태그_목록
    ) {
        return new RunnerPostResponse.Detail(
                러너_게시글_식별자값,
                제목,
                구현_내용,
                궁금한_내용,
                참고_사항,
                풀_리퀘스트,
                마감기한,
                조회수,
                서포터_지원자수,
                리뷰_상태,
                주인_여부,
                서포터_지원_여부,
                태그_목록,
                RunnerResponse.Detail.from(러너)
        );
    }

    public static class RunnerPostDetailBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostDetailBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostDetailBuilder 러너_게시글_식별자값으로_러너_게시글을_조회한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.get(
                    "/api/v1/posts/runner/{runnerPostId}",
                    accessToken,
                    new PathParams(Map.of("runnerPostId", 러너_게시글_식별자값)));
            return this;
        }

        public RunnerPostDetailResponseBuilder 서버_응답() {
            return new RunnerPostDetailResponseBuilder(response);
        }
    }

    public static class RunnerPostDetailResponseBuilder {

        private final ExtractableResponse<Response> response;

        public void 러너_게시글_단건_조회_성공을_검증한다(final RunnerPostResponse.Detail 러너_게시글_응답) {
            final RunnerPostResponse.Detail actual = this.response.as(RunnerPostResponse.Detail.class);

            assertSoftly(softly -> {
                        softly.assertThat(actual.runnerPostId()).isEqualTo(러너_게시글_응답.runnerPostId());
                        softly.assertThat(actual.title()).isEqualTo(러너_게시글_응답.title());
                        softly.assertThat(actual.implementedContents()).isEqualTo(러너_게시글_응답.implementedContents());
                        softly.assertThat(actual.curiousContents()).isEqualTo(러너_게시글_응답.curiousContents());
                        softly.assertThat(actual.postscriptContents()).isEqualTo(러너_게시글_응답.postscriptContents());
                        softly.assertThat(actual.deadline()).isEqualToIgnoringSeconds(러너_게시글_응답.deadline());
                        softly.assertThat(actual.watchedCount()).isEqualTo(러너_게시글_응답.watchedCount());
                        softly.assertThat(actual.applicantCount()).isEqualTo(러너_게시글_응답.applicantCount());
                        softly.assertThat(actual.reviewStatus()).isEqualTo(러너_게시글_응답.reviewStatus());
                        softly.assertThat(actual.tags()).isEqualTo(러너_게시글_응답.tags());
                        softly.assertThat(actual.deadline()).isEqualToIgnoringSeconds(러너_게시글_응답.deadline());
                        softly.assertThat(actual.runnerProfile().name()).isEqualTo(러너_게시글_응답.runnerProfile().name());
                        softly.assertThat(actual.runnerProfile().company()).isEqualTo(러너_게시글_응답.runnerProfile().company());
                        softly.assertThat(actual.runnerProfile().imageUrl()).isEqualTo(러너_게시글_응답.runnerProfile().imageUrl());
                        softly.assertThat(actual.runnerProfile().runnerId()).isEqualTo(러너_게시글_응답.runnerProfile().runnerId());
                        softly.assertThat(actual.watchedCount()).isEqualTo(러너_게시글_응답.watchedCount());
                        softly.assertThat(actual.runnerPostId()).isEqualTo(러너_게시글_응답.runnerPostId());
                    }
            );
        }
        public RunnerPostDetailResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }
    }
}
