package touch.baton.assure.notification.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class NotificationUpdateSupport {

    private NotificationUpdateSupport() {
    }

    public static NotificationUpdateBuilder 클라이언트_요청() {
        return new NotificationUpdateBuilder();
    }

    public static class NotificationUpdateBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public NotificationUpdateBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public NotificationUpdateBuilder 알림_읽음_여부_기록_업데이트를_요청한다(final Long 알림_식별자값) {
            response = AssuredSupport.patch("/api/v1/notifications/{notificationId}",
                    accessToken,
                    new PathParams(Map.of("notificationId", 알림_식별자값))
            );
            return this;
        }

        public NotificationUpdateResponseBuilder 서버_응답() {
            return new NotificationUpdateResponseBuilder(response);
        }
    }

    public static class NotificationUpdateResponseBuilder {

        private final ExtractableResponse<Response> response;

        public NotificationUpdateResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 알림_읽음_여부_기록_업데이트_성공을_검증한다() {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }
}
