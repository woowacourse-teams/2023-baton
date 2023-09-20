package touch.baton.assure.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AssuredSupport {

    public static ExtractableResponse<Response> post(final String uri) {
        return RestAssured
                .given().log().ifValidationFails()
                .when().log().ifValidationFails()
                .post(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> post(final String uri, final Object body) {
        return RestAssured
                .given().log().ifValidationFails()
                .contentType(APPLICATION_JSON_VALUE)
                .body(body)
                .when().log().ifValidationFails()
                .post(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> post(final String uri, final String accessToken) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .when().log().ifValidationFails()
                .post(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> post(final String uri, final String accessToken, final String refreshToken) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .cookie("refreshToken", refreshToken)
                .when().log().ifValidationFails()
                .post(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> post(final String uri, final String accessToken, final Object body) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(body)
                .when().log().ifValidationFails()
                .post(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> post(final String uri,
                                                     final String accessToken,
                                                     final PathParams pathParams,
                                                     final Object body
    ) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .pathParams(pathParams.getValues())
                .body(body)
                .when().log().ifValidationFails()
                .post(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri, final PathParams pathParams) {
        return RestAssured
                .given().log().ifValidationFails()
                .pathParams(pathParams.getValues())
                .when().log().ifValidationFails()
                .get(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri, final String accessToken) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .when().log().ifValidationFails()
                .get(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri,
                                                    final String accessToken,
                                                    final PathParams pathParams
    ) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .pathParams(pathParams.getValues())
                .when().log().ifValidationFails()
                .get(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri, final QueryParams queryParams) {
        return RestAssured
                .given().log().ifValidationFails()
                .queryParams(queryParams.getValues())
                .when().log().ifValidationFails()
                .get(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri, final PathParams pathParams, final QueryParams queryParams) {
        return RestAssured
                .given().log().ifValidationFails()
                .pathParams(pathParams.getValues())
                .queryParams(queryParams.getValues())
                .when().log().ifValidationFails()
                .get(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri, final String accessToken, final QueryParams queryParams) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .queryParams(queryParams.getValues())
                .when().log().ifValidationFails()
                .get(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> patch(final String uri, final String accessToken, final PathParams pathParams) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .pathParams(pathParams.getValues())
                .when().log().ifValidationFails()
                .patch(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> patch(final String uri, final String accessToken, final Object body) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(body)
                .when().log().ifValidationFails()
                .patch(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> patch(final String uri,
                                                      final String accessToken,
                                                      final PathParams pathParams,
                                                      final Object body
    ) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .pathParams(pathParams.getValues())
                .body(body)
                .when().log().ifValidationFails()
                .patch(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> delete(final String uri, final String accessToken, final PathParams pathParams) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .pathParams(pathParams.getValues())
                .when().log().ifValidationFails()
                .delete(uri)
                .then().log().ifError()
                .extract();
    }
}
