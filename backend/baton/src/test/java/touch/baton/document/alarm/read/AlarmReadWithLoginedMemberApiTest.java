package touch.baton.document.alarm.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.query.controller.AlarmQueryController;
import touch.baton.domain.alarm.query.service.AlarmQueryService;
import touch.baton.domain.member.command.Member;
import touch.baton.fixture.domain.AlarmFixture;
import touch.baton.fixture.domain.MemberFixture;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.AlarmReferencedIdFixture.alarmReferencedId;

@WebMvcTest(AlarmQueryController.class)
class AlarmReadWithLoginedMemberApiTest extends RestdocsConfig {

    @MockBean
    private AlarmQueryService alarmQueryService;

    @BeforeEach
    void setUp() {
        restdocsSetUp(new AlarmQueryController(alarmQueryService));
    }

    @DisplayName("로그인한 사용자 알람 목록 조회 API")
    @Test
    void readAlarmsByMemberId() throws Exception {
        // given
        final Member memberHyena = MemberFixture.createHyena();
        final String token = getAccessTokenBySocialId(memberHyena.getSocialId().getValue());
        final Alarm alarm = AlarmFixture.create(memberHyena, alarmReferencedId(1L));

        final Member spyMember = spy(memberHyena);
        when(spyMember.getId()).thenReturn(1L);
        final Alarm spyAlarm = spy(alarm);
        when(spyAlarm.getId()).thenReturn(1L);

        doReturn(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .when(spyAlarm)
                .getCreatedAt();

        // when
        when(alarmQueryService.readAlarmsByMemberId(anyLong(), anyInt())).thenReturn(List.of(spyAlarm));
        when(oauthMemberCommandRepository.findBySocialId(any())).thenReturn(Optional.of(spyMember));

        // then
        mockMvc.perform(get("/api/v1/alarms")
                        .header(AUTHORIZATION, "Bearer " + token)
                        .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT"),
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON_VALUE)
                        ),
                        responseFields(
                                fieldWithPath("data.[].alarmId").type(NUMBER).description("알람 식별자값(id)"),
                                fieldWithPath("data.[].title").type(STRING).description("알람 제목"),
                                fieldWithPath("data.[].message").type(STRING).description("알람 내용"),
                                fieldWithPath("data.[].alarmType").type(STRING).description("알람 타입 (with referencedId)"),
                                fieldWithPath("data.[].referencedId").type(NUMBER).description("알람 연관된 식별자값"),
                                fieldWithPath("data.[].isRead").type(BOOLEAN).description("알람 읽음 여부"),
                                fieldWithPath("data.[].createdAt").type(STRING).description("알람 생성 시간")
                        ))
                ).andDo(print());
    }
}
