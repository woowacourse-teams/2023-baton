package touch.baton.assure.tag;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.QueryParams;
import touch.baton.assure.runnerpost.RunnerPostAssuredCreateSupport;
import touch.baton.assure.runnerpost.RunnerPostAssuredSupport;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.controller.response.TagSearchResponse;
import touch.baton.domain.tag.controller.response.TagSearchResponses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.assure.runnerpost.RunnerPostAssuredCreateSupport.러너_게시글_생성_요청;

@SuppressWarnings("NonAsciiCharacters")
class TagAssuredSupport {

    private TagAssuredSupport() {
    }

    public static TagAssuredSupport.TagClientRequestBuilder 클라이언트_요청() {
        return new TagAssuredSupport.TagClientRequestBuilder();
    }

    public static class TagClientRequestBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public TagAssuredSupport.TagClientRequestBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public TagAssuredSupport.TagClientRequestBuilder 태그_이름을_오름차순으로_10개_검색한다(final String 태그_이름) {
            response = AssuredSupport.get("/api/v1/tags/search", new QueryParams(Map.of("tagName", 태그_이름)));
            return this;
        }

        public TagServerResponseBuilder 서버_응답() {
            return new TagServerResponseBuilder(response);
        }

    }

    public static class TagServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public TagServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 태그_검색_성공을_검증한다(final TagSearchResponses.Detail 검색된_태그_목록) {
            final TagSearchResponses.Detail actual = this.response.as(new TypeRef<>() {

            });

            assertSoftly(softly -> {
                        softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                        softly.assertThat(actual.data()).isEqualTo(검색된_태그_목록.data());
                    }
            );
        }
    }

    public static TagSearchResponses.Detail 태그_검색_Detail_응답(final List<Tag> 검색된_태그_목록) {
        List<TagSearchResponse.TagResponse> 태그_목록_응답 = 검색된_태그_목록.stream()
                .map(tag -> TagSearchResponse.TagResponse.from(tag))
                .toList();
        final TagSearchResponses.Detail 검색된_태그_목록_응답들 = TagSearchResponses.Detail.from(태그_목록_응답);

        return 검색된_태그_목록_응답들;
    }

    public static void 러너_게시글_생성을_성공한다(final String 사용자_액세스_토큰, final List<String> 태그_목록) {
        RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(사용자_액세스_토큰)
                .러너가_러너_게시글을_작성한다(
                        러너_게시글_생성_요청(
                                "테스트용_러너_게시글_제목",
                                태그_목록,
                                "https://test-pull-request.com",
                                LocalDateTime.now().plusHours(100),
                                "테스트용_러너_게시글_구현_내용",
                                "테스트용_러너_게시글_궁금한_내용",
                                "테스트용_러너_게시글_참고_사항"
                        )
                )

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다();
    }
}
