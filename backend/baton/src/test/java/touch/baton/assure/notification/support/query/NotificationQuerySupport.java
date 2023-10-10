package touch.baton.assure.notification.support.query;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.notification.command.Notification;
import touch.baton.domain.notification.query.controller.response.NotificationResponses;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class NotificationQuerySupport {

    private NotificationQuerySupport() {
    }

    public static NotificationQueryBuilder 클라이언트_요청() {
        return new NotificationQueryBuilder();
    }

    public static class NotificationQueryBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public NotificationQueryBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public NotificationQueryBuilder 로그인한_사용자의_알림_목록을_조회한다() {
            response = AssuredSupport.get("/api/v1/notifications", accessToken);
            return this;
        }

        public NotificationQueryResponseBuilder 서버_응답() {
            return new NotificationQueryResponseBuilder(response);
        }
    }

    public static class NotificationQueryResponseBuilder {

        private final ExtractableResponse<Response> response;

        public NotificationQueryResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 로그인한_사용자의_알림_목록_조회_성공을_검증한다(final List<Notification> 알림_목록) {
            final NotificationResponses.SimpleNotifications 조회된_알림_응답_목록 = this.response.as(new TypeRef<>() {
            });

            assertThat(조회된_알림_응답_목록).isEqualTo(NotificationResponses.SimpleNotifications.from(알림_목록));
        }
    }
}
