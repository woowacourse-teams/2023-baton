package touch.baton.assure.notification.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class NotificationDeleteSupport {

    private NotificationDeleteSupport() {
    }

    public static NotificationDeleteBuilder 클라이언트_요청() {
        return new NotificationDeleteBuilder();
    }

    public static class NotificationDeleteBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public NotificationDeleteBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public NotificationDeleteBuilder 알림_삭제에_성공한다(final Long 알림_식별자값) {
            response = AssuredSupport.delete("/api/v1/notifications/{notificationId}",
                    accessToken,
                    new PathParams(Map.of("notificationId", 알림_식별자값))
            );
            return this;
        }

        public NotificationDeleteResponseBuilder 서버_응답() {
            return new NotificationDeleteResponseBuilder(response);
        }
    }

    public static class NotificationDeleteResponseBuilder {

        private final ExtractableResponse<Response> response;

        public NotificationDeleteResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 알림_삭제_성공을_검증한다() {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }
}
