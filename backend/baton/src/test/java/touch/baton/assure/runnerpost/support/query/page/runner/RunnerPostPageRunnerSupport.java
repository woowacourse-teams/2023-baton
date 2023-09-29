package touch.baton.assure.runnerpost.support.query.page.runner;

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
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostPageRunnerSupport {

    private RunnerPostPageRunnerSupport() {
    }

    public static RunnerPostPageRunnerBuilder 클라이언트_요청() {
        return new RunnerPostPageRunnerBuilder();
    }


    public static RunnerPostResponse.SimpleInMyPage 마이페이지_러너_게시글_SimpleInMyPage_응답(final RunnerPost 러너_게시글,
                                                                                   final Long 지원한_서포터_식별자값,
                                                                                   final int 조회수,
                                                                                   final long 지원한_서포터_수,
                                                                                   final ReviewStatus 리뷰_상태,
                                                                                   final List<String> 러너_게시글_태그_목록
    ) {
        return new RunnerPostResponse.SimpleInMyPage(
                러너_게시글.getId(),
                지원한_서포터_식별자값,
                러너_게시글.getTitle().getValue(),
                러너_게시글.getDeadline().getValue(),
                러너_게시글_태그_목록,
                조회수,
                지원한_서포터_수,
                리뷰_상태.name(),
                러너_게시글.getIsReviewed().getValue()
        );
    }

    public static PageResponse<RunnerPostResponse.SimpleInMyPage> 마이페이지_러너_게시글_페이지_응답(final Pageable 페이징_정보,
                                                                                      final List<RunnerPostResponse.SimpleInMyPage> 마이페이지_러너_게시글_목록
    ) {
        final Page<RunnerPostResponse.SimpleInMyPage> 페이징된_마이페이지_러너_게시글 = new PageImpl<>(마이페이지_러너_게시글_목록, 페이징_정보, 마이페이지_러너_게시글_목록.size());
        final PageResponse<RunnerPostResponse.SimpleInMyPage> 페이징된_마이페이지_러너_게시글_응답 = PageResponse.from(페이징된_마이페이지_러너_게시글);

        return 페이징된_마이페이지_러너_게시글_응답;
    }

    public static class RunnerPostPageRunnerBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public RunnerPostPageRunnerBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public RunnerPostPageRunnerBuilder 마이페이지_러너_게시글_페이징을_조회한다(final ReviewStatus 리뷰_상태, final Pageable 페이징_정보) {
            final Map<String, Object> queryParams = Map.of(
                    "reviewStatus", 리뷰_상태,
                    "size", 페이징_정보.getPageSize(),
                    "page", 페이징_정보.getPageNumber()
            );

            response = AssuredSupport.get("/api/v1/posts/runner/me/runner", accessToken, new QueryParams(queryParams));
            return this;
        }

        public RunnerPostPageRunnerResponseBuilder 서버_응답() {
            return new RunnerPostPageRunnerResponseBuilder(response);
        }
    }

    public static class RunnerPostPageRunnerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public RunnerPostPageRunnerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 마이페이지_러너_게시글_페이징_조회_성공을_검증한다(final PageResponse<RunnerPostResponse.SimpleInMyPage> 마이페이지_러너_게시글_페이징_응답) {
            final PageResponse<RunnerPostResponse.SimpleInMyPage> actual = this.response.as(new TypeRef<>() {

            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual.data()).isEqualTo(마이페이지_러너_게시글_페이징_응답.data());
                    }
            );
        }
    }
}
