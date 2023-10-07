package touch.baton.assure.alarm.support.command;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.assure.common.PathParams;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class AlarmDeleteSupport {

    private AlarmDeleteSupport() {
    }

    public static AlarmDeleteBuilder 클라이언트_요청() {
        return new AlarmDeleteBuilder();
    }

    public static class AlarmDeleteBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public AlarmDeleteBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public AlarmDeleteBuilder 알람_삭제에_성공한다(final Long 알람_식별자값) {
            response = AssuredSupport.delete("/api/v1/alarms/{alarmId}",
                    accessToken,
                    new PathParams(Map.of("alarmId", 알람_식별자값))
            );
            return this;
        }

        public AlarmDeleteResponseBuilder 서버_응답() {
            return new AlarmDeleteResponseBuilder(response);
        }
    }

    public static class AlarmDeleteResponseBuilder {

        private final ExtractableResponse<Response> response;

        public AlarmDeleteResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 알람_삭제_성공을_검증한다() {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }
}
