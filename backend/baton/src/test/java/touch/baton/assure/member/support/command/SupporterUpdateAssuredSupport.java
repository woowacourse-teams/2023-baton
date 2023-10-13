package touch.baton.assure.member.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.response.ErrorResponse;
import touch.baton.domain.member.command.service.dto.SupporterUpdateRequest;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
public class SupporterUpdateAssuredSupport {

    private SupporterUpdateAssuredSupport() {
    }

    public static SupporterUpdateBuilder 클라이언트_요청() {
        return new SupporterUpdateBuilder();
    }

    public static SupporterUpdateRequest 서포터_본인_정보_수정_요청(final String 이름,
                                                         final String 회사,
                                                         final String 서포터_자기소개글,
                                                         final List<String> 서포터_기술_태그_목록
    ) {
        return new SupporterUpdateRequest(이름, 회사, 서포터_자기소개글, 서포터_기술_태그_목록);
    }

    public static class SupporterUpdateBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public SupporterUpdateBuilder 액세스_토큰으로_로그인_한다(final String 액세스_토큰) {
            accessToken = 액세스_토큰;
            return this;
        }

        public SupporterUpdateBuilder 서포터_본인_프로필을_수정한다(final SupporterUpdateRequest 서포터_업데이트_요청) {
            response = AssuredSupport.patch("/api/v1/profile/supporter/me", accessToken, 서포터_업데이트_요청);
            return this;
        }

        public SupporterUpdateResponseBuilder 서버_응답() {
            return new SupporterUpdateResponseBuilder(response);
        }
    }

    public static class SupporterUpdateResponseBuilder {

        private final ExtractableResponse<Response> response;

        public SupporterUpdateResponseBuilder(final ExtractableResponse<Response> 응답) {
            this.response = 응답;
        }

        public void 서포터_본인_프로필_수정_성공을_검증한다(final HttpStatus HTTP_STATUS) {
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(HTTP_STATUS.value());
                softly.assertThat(response.header("Location")).isNotNull();
            });
        }

        public void 서포터_본인_프로필_수정_실패를_검증한다(final ClientErrorCode 클라이언트_에러_코드) {
            final ErrorResponse actual = this.response.as(ErrorResponse.class);

            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(클라이언트_에러_코드.getHttpStatus().value());
                softly.assertThat(actual.errorCode()).isEqualTo(클라이언트_에러_코드.getErrorCode());
            });
        }
    }
}
