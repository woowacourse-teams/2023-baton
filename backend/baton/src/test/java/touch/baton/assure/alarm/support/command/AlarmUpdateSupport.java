package touch.baton.assure.alarm.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class AlarmUpdateSupport {

    private AlarmUpdateSupport() {
    }

    public static AlarmUpdateBuilder 클라이언트_요청() {
        return new AlarmUpdateBuilder();
    }

    public static class AlarmUpdateBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public AlarmUpdateBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public AlarmUpdateBuilder 알람_읽음_여부_기록_업데이트를_요청한다(final Long 알람_식별자값) {
            response = AssuredSupport.patch("/api/v1/alarms/{alarmId}",
                    accessToken,
                    new PathParams(Map.of("alarmId", 알람_식별자값))
            );
            return this;
        }

        public AlarmUpdateResponseBuilder 서버_응답() {
            return new AlarmUpdateResponseBuilder(response);
        }
    }

    public static class AlarmUpdateResponseBuilder {

        private final ExtractableResponse<Response> response;

        public AlarmUpdateResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 알람_읽음_여부_기록_업데이트_성공을_검증한다() {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }
}
