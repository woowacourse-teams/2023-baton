package touch.baton.assure.oauth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.oauth.OauthType;

import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

public class OauthAssuredSupport {

    private OauthAssuredSupport() {
    }

    public static OauthClientRequestBuilder 클라이언트_요청() {
        return new OauthClientRequestBuilder();
    }

    public static class OauthClientRequestBuilder {

        private ExtractableResponse<Response> response;

        public OauthClientRequestBuilder 소셜_로그인을_위한_리다이렉트_URL을_요청한다(final OauthType 소셜_타입) {
            response = RestAssured
                    .given().log().ifValidationFails()
                    .pathParams(Map.of("oauthType", 소셜_타입))
                    .redirects().follow(false)
                    .when().log().ifValidationFails()
                    .get("/api/v1/oauth/{oauthType}")
                    .then().log().ifError()
                    .extract();

            return this;
        }

        public OauthClientRequestBuilder AuthCode를_통해_소셜_토큰을_발급_받은_후_사용자를_회원가입_한다(final OauthType 소셜_타입, final String 사용자의_AuthCode) {
            response = AssuredSupport.get(
                    "/api/v1/oauth/login/{oauthType}",
                    new PathParams(Map.of("oauthType", 소셜_타입)),
                    new QueryParams(Map.of("code", 사용자의_AuthCode))
            );

            return this;
        }

        public OauthServerResponseBuilder 서버_응답() {
            return new OauthServerResponseBuilder(response);
        }
    }

    public static class OauthServerResponseBuilder {

        private final ExtractableResponse<Response> response;

        public OauthServerResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public OauthServerResponseBuilder 소셜_로그인을_위한_리다이렉트_URL_요청_성공을_검증한다() {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.FOUND.value());
            });

            return this;
        }

        public OauthServerResponseBuilder AuthCode를_통해_소셜_토큰_발급_및_사용자_회원가입에_성공한다() {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                softly.assertThat(response.header(AUTHORIZATION)).isNotBlank();
                softly.assertThat(response.header(SET_COOKIE)).isNotBlank();
            });

            return this;
        }

        public String 액세스_토큰을_반환한다() {
            return response.header(AUTHORIZATION);
        }
    }
}
