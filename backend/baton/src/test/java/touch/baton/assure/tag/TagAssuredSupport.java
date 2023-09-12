package touch.baton.assure.tag;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.runnerpost.controller.response.TagSearchResponse;
import touch.baton.domain.runnerpost.controller.response.TagSearchResponses;
import touch.baton.domain.tag.Tag;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class TagAssuredSupport {

    private TagAssuredSupport() {
    }

    public static TagAssuredSupport.TagClientRequestBuilder 클라이언트_요청() {
        return new TagAssuredSupport.TagClientRequestBuilder();
    }

    public static class TagClientRequestBuilder {

        private ExtractableResponse<Response> response;

        public TagAssuredSupport.TagClientRequestBuilder 태그를_이름으로_오름차순일때_10개_검색한다(final String 태그_이름) {
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
                .map(tag -> TagSearchResponse.TagResponse.of(tag.getId(), tag.getTagName().getValue()))
                .toList();
        final TagSearchResponses.Detail 검색된_태그_목록_응답들 = TagSearchResponses.Detail.from(태그_목록_응답);

        return 검색된_태그_목록_응답들;
    }

}
