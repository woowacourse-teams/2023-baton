package touch.baton.assure.runnerpost;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.domain.common.response.ErrorResponse;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.controller.response.RunnerResponse;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.controller.response.SupporterRunnerPostResponses;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.LOCATION;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostAssuredSupport {

    private RunnerPostAssuredSupport() {
    }

    public static RunnerPostClientRequestBuilder 클라이언트_요청() {
        return new RunnerPostClientRequestBuilder();
    }

    public static RunnerPostResponse.Detail 러너_게시글_Detail_응답(final Long 러너_게시글_식별자값,
                                                             final String 제목,
                                                             final String 내용,
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
                내용,
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

    public static PageResponse<RunnerPostResponse.ReferencedBySupporter> 서포터가_리뷰_완료한_러너_게시글_응답(final Pageable 페이징_정보,
                                                                                               final List<RunnerPostResponse.ReferencedBySupporter> 서포터가_연관된_러너_게시글_목록
    ) {
        final Page<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_연관된_러너_게시글 = new PageImpl<>(서포터가_연관된_러너_게시글_목록, 페이징_정보, 서포터가_연관된_러너_게시글_목록.size());
        final PageResponse<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_연관된_러너_게시글_응답 = PageResponse.from(페이징된_서포터가_연관된_러너_게시글);

        return 페이징된_서포터가_연관된_러너_게시글_응답;
    }

    public static PageResponse<RunnerPostResponse.Simple> 러너_게시글_전체_조회_응답(final Pageable 페이징_정보,
                                                                          final List<RunnerPostResponse.Simple> 러너_게시글_목록
    ) {
        final Page<RunnerPostResponse.Simple> 페이징된_러너_게시글 = new PageImpl<>(러너_게시글_목록, 페이징_정보, 러너_게시글_목록.size());
        final PageResponse<RunnerPostResponse.Simple> 페이징된_러너_게시글_응답 = PageResponse.from(페이징된_러너_게시글);

        return 페이징된_러너_게시글_응답;
    }

    public static class RunnerPostClientRequestBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostClientRequestBuilder 토큰으로_로그인한다(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public RunnerPostClientRequestBuilder 러너_게시글_등록_요청한다(final RunnerPostCreateRequest 게시글_생성_요청) {
            response = AssuredSupport.post("/api/v1/posts/runner", 게시글_생성_요청, accessToken);
            return this;
        }

        public RunnerPostClientRequestBuilder 러너_게시글_식별자값으로_러너_게시글을_조회한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.get("/api/v1/posts/runner/{runnerPostId}", "runnerPostId", 러너_게시글_식별자값, accessToken);
            return this;
        }

        public RunnerPostClientRequestBuilder 러너_게시글_식별자값으로_서포터_러너_게시글을_조회한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.get("/api/v1/posts/runner/{runnerPostId}/supporters", "runnerPostId", 러너_게시글_식별자값, accessToken);
            return this;
        }

        public RunnerPostClientRequestBuilder 서포터와_연관된_러너_게시글_페이징을_조회한다(final Long 서포터_식별자값,
                                                                        final ReviewStatus 리뷰_진행_상태,
                                                                        final Pageable 페이징_정보
        ) {
            final Map<String, Object> queryParams = Map.of(
                    "supporterId", 서포터_식별자값,
                    "reviewStatus", 리뷰_진행_상태,
                    "size", 페이징_정보.getPageSize(),
                    "page", 페이징_정보.getPageNumber()
            );

            response = AssuredSupport.get("/api/v1/posts/runner/search", queryParams);
            return this;
        }

        public RunnerPostClientRequestBuilder 전체_러너_게시글_페이징을_조회한다(final Pageable 페이징_정보) {
            final Map<String, Object> queryParams = Map.of(
                    "size", 페이징_정보.getPageSize(),
                    "page", 페이징_정보.getPageNumber()
            );

            response = AssuredSupport.get("/api/v1/posts/runner", queryParams);
            return this;
        }

        public RunnerPostClientRequestBuilder 서포터가_리뷰를_완료하고_리뷰완료_버튼을_누른다(final Long 게시글_식별자) {
            response = AssuredSupport.patch("/api/v1/posts/runner/{runnerPostId}/done", "runnerPostId", 게시글_식별자, accessToken);
            return this;
        }

        public RunnerPostClientRequestBuilder 러너_게시글_식별자값으로_러너_게시글을_삭제한다(final Long 러너_게시글_식별자값) {
            response = AssuredSupport.delete("/api/v1/posts/runner/{runnerPostId}", accessToken, "runnerPostId", 러너_게시글_식별자값);
            return this;
        }

        public RunnerPostClientRequestBuilder 러너가_서포터를_선택한다(final Long 게시글_식별자값,
                                                            final RunnerPostUpdateRequest.SelectSupporter 서포터_선택_요청_정보
        ) {
            response = AssuredSupport.patch("/api/v1/posts/runner/{runnerPostId}/supporters",
                    "runnerPostId", 게시글_식별자값,
                    서포터_선택_요청_정보,
                    accessToken
            );
            return this;
        }

        public RunnerPostClientRequestBuilder 로그인한_서포터의_러너_게시글_페이징을_조회한다(final ReviewStatus 리뷰_진행_상태,
                                                                         final Pageable 페이징_정보
        ) {
            final Map<String, Object> queryParams = Map.of(
                    "reviewStatus", 리뷰_진행_상태,
                    "size", 페이징_정보.getPageSize(),
                    "page", 페이징_정보.getPageNumber()
            );

            response = AssuredSupport.get("/api/v1/posts/runner/me/supporter", accessToken, queryParams);
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
                        softly.assertThat(actual.runnerPostId()).isEqualTo(러너_게시글_응답.runnerPostId());
                        softly.assertThat(actual.title()).isEqualTo(러너_게시글_응답.title());
                        softly.assertThat(actual.contents()).isEqualTo(러너_게시글_응답.contents());
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

        public void 서포터와_연관된_러너_게시글_페이징_조회_성공을_검증한다(final PageResponse<RunnerPostResponse.ReferencedBySupporter> 서포터와_연관된_러너_게시글_페이징_응답) {
            final PageResponse<RunnerPostResponse.ReferencedBySupporter> actual = this.response.as(new TypeRef<PageResponse<RunnerPostResponse.ReferencedBySupporter>>() {
            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual.data()).isEqualTo(서포터와_연관된_러너_게시글_페이징_응답.data());
                    }
            );
        }

        public void 전체_러너_게시글_페이징_조회_성공을_검증한다(final PageResponse<RunnerPostResponse.Simple> 전체_러너_게시글_페이징_응답) {
            final PageResponse<RunnerPostResponse.Simple> actual = this.response.as(new TypeRef<PageResponse<RunnerPostResponse.Simple>>() {

            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual.data()).isEqualTo(전체_러너_게시글_페이징_응답.data());
                    }
            );
        }

        public void 서포터_러너_게시글_조회_성공을_검증한다(final SupporterRunnerPostResponses.Detail 전체_러너_게시글_페이징_응답) {
            final SupporterRunnerPostResponses.Detail actual = this.response.as(new TypeRef<SupporterRunnerPostResponses.Detail>() {

            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual.data()).isEqualTo(전체_러너_게시글_페이징_응답.data());
                    }
            );
        }

        public void 러너_게시글_삭제_성공을_검증한다(final HttpStatus HTTP_STATUS) {
            assertThat(response.statusCode()).isEqualTo(HTTP_STATUS.value());
        }

        public void 러너_게시글_삭제_실패를_검증한다(final HttpStatus HTTP_STATUS) {
            assertThat(response.statusCode()).isEqualTo(HTTP_STATUS.value());
        }

        public void 러너_게시글에_서포터가_성공적으로_선택되었는지_확인한다(final HttpStatusAndLocationHeader httpStatusAndLocationHeader) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(httpStatusAndLocationHeader.getHttpStatus().value());
                softly.assertThat(response.header(LOCATION)).contains(httpStatusAndLocationHeader.getLocation());
            });
        }

        public void 러너_게시글이_성공적으로_리뷰_완료_상태인지_확인한다(final HttpStatusAndLocationHeader httpStatusAndLocationHeader) {
            assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(httpStatusAndLocationHeader.getHttpStatus().value());
                        softly.assertThat(response.header(LOCATION)).contains(httpStatusAndLocationHeader.getLocation());
                    }
            );
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
