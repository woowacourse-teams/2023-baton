package touch.baton.assure.tag.support.query;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.domain.tag.query.controller.response.TagSearchResponses;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class TagQuerySupport {

    private TagQuerySupport() {
    }

    public static TagQueryBuilder 클라이언트_요청() {
        return new TagQueryBuilder();
    }

    public static class TagQueryBuilder {

        private ExtractableResponse<Response> response;

        public TagQueryBuilder 입력된_문자열로_태그_목록을_검색한다(final TagReducedName 요청할_태그_이름) {
            response = AssuredSupport.get("/api/v1/tags/search", new QueryParams(Map.of("tagName", 요청할_태그_이름.getValue())));
            return this;
        }

        public TagQueryResponseBuilder 서버_응답() {
            return new TagQueryResponseBuilder(response);
        }
    }

    public static class TagQueryResponseBuilder {

        private final ExtractableResponse<Response> response;

        public TagQueryResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 입력된_문자열로_태그_목록_검색_성공을_검증한다(final TagSearchResponses.Detail 검색된_태그_목록) {
            final TagSearchResponses.Detail actual = this.response.as(new TypeRef<>() {
            });

            assertSoftly(softly -> {
                softly.assertThat(this.response.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(actual.data()).isEqualTo(검색된_태그_목록.data());
            });
        }
    }
}
