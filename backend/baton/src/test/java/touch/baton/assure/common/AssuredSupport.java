package touch.baton.assure.common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AssuredSupport {

    public static ExtractableResponse<Response> post(final String uri, final Object params) {
        return RestAssured
                .given().log().ifValidationFails()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().log().ifValidationFails()
                .post(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri,
                                                    final String pathParamName,
                                                    final Long id,
                                                    final String accessToken
    ) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .when().log().ifValidationFails()
                .pathParam(pathParamName, id)
                .get(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri, final String pathParamName, final Long id) {
        return RestAssured
                .given().log().ifValidationFails()
                .when().log().ifValidationFails()
                .pathParam(pathParamName, id)
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

    public static ExtractableResponse<Response> get(final String uri, final Map<String, Object> queryParams) {
        return RestAssured
                .given().log().ifValidationFails()
                .contentType(APPLICATION_JSON_VALUE)
                .queryParams(queryParams)
                .when().log().ifValidationFails()
                .get(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> get(final String uri,
                                                    final String accessToken,
                                                    final Map<String, Object> queryParams) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .queryParams(queryParams)
                .when().log().ifValidationFails()
                .get(uri)
                .then().log().ifError()
                .extract();
    }


    public static ExtractableResponse<Response> patch(final String uri, final String accessToken, final Object params) {
        return RestAssured
                .given().log().ifValidationFails()
                .auth().preemptive().oauth2(accessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().log().ifValidationFails()
                .patch(uri)
                .then().log().ifError()
                .extract();
    }

    public static ExtractableResponse<Response> delete(final String uri, final String pathParamName, final Long id) {
        return RestAssured
                .given().log().ifValidationFails()
                .when().log().ifValidationFails()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam(pathParamName, id)
                .delete(uri)
                .then().log().ifError()
                .extract();
    }
}
