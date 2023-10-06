package touch.baton.document.alarm.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.command.controller.AlarmCommandController;
import touch.baton.domain.alarm.command.service.AlarmCommandService;
import touch.baton.domain.member.command.Member;
import touch.baton.fixture.domain.AlarmFixture;
import touch.baton.fixture.domain.MemberFixture;

import java.util.Optional;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.AlarmReferencedIdFixture.alarmReferencedId;

@WebMvcTest(AlarmCommandController.class)
class AlarmUpdateApiTest extends RestdocsConfig {

    @MockBean
    private AlarmCommandService alarmCommandService;

    @BeforeEach
    void setUp() {
        restdocsSetUp(new AlarmCommandController(alarmCommandService));
    }

    @DisplayName("알람 읽기 여부 기록 API")
    @Test
    void updateAlarmIsReadTrueByMember() throws Exception {
        // given
        final Member memberHyena = MemberFixture.createHyena();
        final String token = getAccessTokenBySocialId(memberHyena.getSocialId().getValue());
        final Alarm alarm = AlarmFixture.create(memberHyena, alarmReferencedId(1L));

        // when
        willDoNothing().given(alarmCommandService).updateAlarmIsReadTrueByMember(any(Member.class), anyLong());
        when(oauthMemberCommandRepository.findBySocialId(any())).thenReturn(Optional.ofNullable(memberHyena));

        final Alarm spyAlarm = spy(alarm);
        when(spyAlarm.getId()).thenReturn(1L);

        // then
        mockMvc.perform(patch("/api/v1/alarms/{alarmId}", spyAlarm.getId())
                        .header(AUTHORIZATION, "Bearer " + token)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT"),
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON_VALUE)
                        )
                ));
    }
}
