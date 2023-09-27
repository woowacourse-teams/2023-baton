package touch.baton.assure.runnerpost.support.query.page.supporter;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostPageSupporterSupport {

    private RunnerPostPageSupporterSupport() {
    }

    public static RunnerPostPageSupporterBuilder 클라이언트_요청() {
        return new RunnerPostPageSupporterBuilder();
    }

    public static RunnerPostResponse.ReferencedBySupporter 서포터와_연관된_러너_게시글_응답(final RunnerPost 러너_게시글,
                                                                              final List<String> 러너_게시글_태그_목록,
                                                                              final int 조회수,
                                                                              final long 러너_게시글에_지원한_서포터수,
                                                                              final ReviewStatus 리뷰_상태
    ) {
        return new RunnerPostResponse.ReferencedBySupporter(
                러너_게시글.getId(),
                러너_게시글.getTitle().getValue(),
                러너_게시글.getDeadline().getValue(),
                러너_게시글_태그_목록,
                조회수,
                러너_게시글에_지원한_서포터수,
                리뷰_상태.name()
        );
    }

    public static PageResponse<RunnerPostResponse.ReferencedBySupporter> 서포터와_연관된_러너_게시글_페이징_응답(final Pageable 페이징_정보,
                                                                                                final List<RunnerPostResponse.ReferencedBySupporter> 서포터가_연관된_러너_게시글_목록
    ) {
        final Page<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_연관된_러너_게시글 = new PageImpl<>(서포터가_연관된_러너_게시글_목록, 페이징_정보, 서포터가_연관된_러너_게시글_목록.size());
        final PageResponse<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_연관된_러너_게시글_응답 = PageResponse.from(페이징된_서포터가_연관된_러너_게시글);

        return 페이징된_서포터가_연관된_러너_게시글_응답;
    }

    public static class RunnerPostPageSupporterBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostPageSupporterBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostPageSupporterBuilder 서포터와_연관된_러너_게시글_페이징을_조회한다(final Long 서포터_식별자값,
                                                                                                 final ReviewStatus 리뷰_진행_상태,
                                                                                                 final Pageable 페이징_정보
        ) {
            final Map<String, Object> queryParams = Map.of(
                    "supporterId", 서포터_식별자값,
                    "reviewStatus", 리뷰_진행_상태,
                    "size", 페이징_정보.getPageSize(),
                    "page", 페이징_정보.getPageNumber()
            );

            response = AssuredSupport.get("/api/v1/posts/runner/search", new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageSupporterBuilder 로그인한_서포터의_러너_게시글_페이징을_조회한다(final ReviewStatus 리뷰_진행_상태,
                                                                                 final Pageable 페이징_정보
        ) {
            final Map<String, Object> queryParams = Map.of(
                    "reviewStatus", 리뷰_진행_상태,
                    "size", 페이징_정보.getPageSize(),
                    "page", 페이징_정보.getPageNumber()
            );

            response = AssuredSupport.get("/api/v1/posts/runner/me/supporter", accessToken, new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageSupporterResponseBuilder 서버_응답() {
            return new RunnerPostPageSupporterResponseBuilder(response);
        }
    }

    public static class RunnerPostPageSupporterResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostPageSupporterResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 서포터와_연관된_러너_게시글_페이징_조회_성공을_검증한다(final PageResponse<RunnerPostResponse.ReferencedBySupporter> 서포터와_연관된_러너_게시글_페이징_응답) {
            final PageResponse<RunnerPostResponse.ReferencedBySupporter> actual = this.response.as(new TypeRef<>() {

            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual.data()).isEqualTo(서포터와_연관된_러너_게시글_페이징_응답.data());
                    }
            );
        }
    }
}
