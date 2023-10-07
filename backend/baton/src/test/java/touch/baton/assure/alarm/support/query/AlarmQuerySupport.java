package touch.baton.assure.alarm.support.query;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.query.controller.response.AlarmResponses;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class AlarmQuerySupport {

    private AlarmQuerySupport() {
    }

    public static AlarmQueryBuilder 클라이언트_요청() {
        return new AlarmQueryBuilder();
    }

    public static class AlarmQueryBuilder {

        private ExtractableResponse<Response> response;

        private String accessToken;

        public AlarmQueryBuilder 액세스_토큰으로_로그인한다(final String 액세스_토큰) {
            this.accessToken = 액세스_토큰;
            return this;
        }

        public AlarmQueryBuilder 로그인한_사용자의_알람_목록을_조회한다() {
            response = AssuredSupport.get("/api/v1/alarms", accessToken);
            return this;
        }

        public AlarmQueryResponseBuilder 서버_응답() {
            return new AlarmQueryResponseBuilder(response);
        }
    }

    public static class AlarmQueryResponseBuilder {

        private final ExtractableResponse<Response> response;

        public AlarmQueryResponseBuilder(final ExtractableResponse<Response> response) {
            this.response = response;
        }

        public void 로그인한_사용자의_알람_목록_조회_성공을_검증한다(final List<Alarm> 알람_목록) {
            final AlarmResponses.SimpleAlarms 조회된_알람_응답_목록 = this.response.as(new TypeRef<>() {
            });

            assertThat(조회된_알람_응답_목록).isEqualTo(AlarmResponses.SimpleAlarms.from(알람_목록));
        }
    }
}
