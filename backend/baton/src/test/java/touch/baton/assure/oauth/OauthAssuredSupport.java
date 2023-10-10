package touch.baton.assure.oauth;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;
import touch.baton.assure.common.QueryParams;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.OauthType;
import touch.baton.domain.oauth.command.token.AccessToken;
import touch.baton.domain.oauth.command.token.ExpireDate;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;
import touch.baton.domain.oauth.command.token.Tokens;

import java.time.LocalDateTime;
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

    @SuppressWarnings("NonAsciiCharacters")
    public static class OauthClientRequestBuilder {

        private ExtractableResponse<Response> response;
        private String accessToken;

        public OauthClientRequestBuilder 액세스_토큰으로_로그인_한다(final String 액세스_토큰) {
            accessToken = 액세스_토큰;

            return this;
        }

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

        public OauthClientRequestBuilder 기간이_만료된_액세스_토큰으로_프로필_조회하려_한다() {
            response = AssuredSupport.get("/api/v1/profile/runner/me", accessToken);

            return this;
        }

        public OauthClientRequestBuilder 기간_만료_액세스_토큰과_리프레시_토큰으로_리프레시_요청한다(final String 기간_만료_액세스_토큰, final String 리프레시_토큰) {
            response = AssuredSupport.post("/api/v1/oauth/refresh", 기간_만료_액세스_토큰, 리프레시_토큰);

            return this;
        }

        public OauthClientRequestBuilder 리프레시_토큰_없이_액세스_토큰만으로_리프레시_요청한다() {
            response = AssuredSupport.post("/api/v1/oauth/refresh", accessToken);

            return this;
        }

        public OauthClientRequestBuilder 리프레시를_요청한다() {
            response = AssuredSupport.post("/api/v1/oauth/refresh");

            return this;
        }

        public OauthClientRequestBuilder 로그아웃을_요청한다(final AccessToken 액세스_토큰) {
            response = AssuredSupport.delete("/api/v1/oauth/logout", 액세스_토큰.getValue());

            return this;
        }

        public OauthClientRequestBuilder 액세스_토큰_없이_로그아웃을_요청한다() {
            response = AssuredSupport.delete("/api/v1/oauth/logout");

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

        public Tokens 액세스_토큰과_리프레시_토큰을_반환한다(final Member ethan) {
            final String accessToken = response.header(AUTHORIZATION);

            final LocalDateTime expireDate = LocalDateTime.now().plusDays(30);
            final RefreshToken refreshToken = RefreshToken.builder()
                    .member(ethan)
                    .token(new Token(response.cookie("refreshToken")))
                    .expireDate(new ExpireDate(expireDate))
                    .build();

            return new Tokens(new AccessToken(accessToken), refreshToken);
        }

        public void 새로운_액세스_토큰과_리프레시_토큰을_반환한다() {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
                softly.assertThat(response.header(AUTHORIZATION)).isNotBlank();
                softly.assertThat(response.header(SET_COOKIE)).isNotBlank();
            });
        }

        public void 오류가_발생한다(final ClientErrorCode clientErrorCode) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(clientErrorCode.getHttpStatus().value());
                softly.assertThat(response.jsonPath().getString("errorCode")).isEqualTo(clientErrorCode.getErrorCode());
            });
        }

        public void 로그아웃이_성공한다() {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            });
        }
    }
}
