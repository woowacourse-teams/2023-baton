package touch.baton.assure.runnerpost;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostAssuredSupport {

    private RunnerPostAssuredSupport() {
    }

    public static RunnerPostClientRequestBuilder 클라이언트_요청() {
        return new RunnerPostClientRequestBuilder();
    }

    public static PageResponse<RunnerPostResponse.ReferencedBySupporter> 서포터가_리뷰_완료한_러너_게시글_응답(final Pageable 페이징_정보,
                                                                                               final List<RunnerPostResponse.ReferencedBySupporter> 서포터가_연관된_러너_게시글_목록
    ) {
        final Page<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_연관된_러너_게시글 = new PageImpl<>(서포터가_연관된_러너_게시글_목록, 페이징_정보, 서포터가_연관된_러너_게시글_목록.size());
        final PageResponse<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_연관된_러너_게시글_응답 = PageResponse.from(페이징된_서포터가_연관된_러너_게시글);

        return 페이징된_서포터가_연관된_러너_게시글_응답;
    }

    public static class RunnerPostClientRequestBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostClientRequestBuilder 토큰으로_로그인한다(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public RunnerPostClientRequestBuilder 러너_게시글_식별자값으로_러너_게시글을_조회한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.get("/api/v1/posts/runner/{runnerPostId}", "runnerPostId", 러너_게시글_식별자값, accessToken);
            return this;
        }

        public RunnerPostClientRequestBuilder 서포터와_연관된_러너_게시글_페이징을_조회한다(final Long 서포터_식별자값,
                                                                        final ReviewStatus 리뷰_상태,
                                                                        final Pageable 페이징_정보
        ) {
            final Map<String, Object> queryParams = Map.of(
                    "supporterId", 서포터_식별자값,
                    "reviewStatus", 리뷰_상태,
                    "size", 페이징_정보.getPageSize(),
                    "page", 페이징_정보.getPageNumber()
            );

            response = AssuredSupport.get("/api/v1/posts/runner/search", queryParams);
            return this;
        }

        public RunnerPostClientRequestBuilder 러너_게시글_식별자값으로_러너_게시글을_삭제한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.delete("/api/v1/posts/runner/{runnerPostId}", "runnerPostId", 러너_게시글_식별자값);
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

        public void 러너_게시글_단건_조회_성공을_검증한다(final RunnerPostResponse.Detail 러너_게시글_응답) {
            final RunnerPostResponse.Detail actual = this.response.as(RunnerPostResponse.Detail.class);

            assertSoftly(softly -> {
                        softly.assertThat(actual.title()).isEqualTo(러너_게시글_응답.title());
                        softly.assertThat(actual.contents()).isEqualTo(러너_게시글_응답.contents());
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

        public void 서포터와_연관된_러너_게시글_페이징_조회_성공을_검증한다(final PageResponse<RunnerPostResponse.ReferencedBySupporter> 서포터와_연관된_러너_게시글_페이징_응답) {
            final PageResponse<RunnerPostResponse.ReferencedBySupporter> actual = this.response.as(new TypeRef<PageResponse<RunnerPostResponse.ReferencedBySupporter>>() {
            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual.data()).isEqualTo(서포터와_연관된_러너_게시글_페이징_응답.data());
                    }
            );
        }

        public void 러너_게시글_삭제_성공을_검증한다(final HttpStatus HTTP_STATUS) {
            assertThat(response.statusCode())
                    .isEqualTo(HTTP_STATUS.value());
        }
    }
}
