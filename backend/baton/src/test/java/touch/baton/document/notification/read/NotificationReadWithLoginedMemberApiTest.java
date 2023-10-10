package touch.baton.document.notification.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.notification.command.Notification;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.NotificationFixture;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.NotificationReferencedIdFixture.notificationReferencedId;

class NotificationReadWithLoginedMemberApiTest extends RestdocsConfig {

    @DisplayName("로그인한 사용자 알림 목록 조회 API")
    @Test
    void readNotificationsByMemberId() throws Exception {
        // given
        final Member memberHyena = MemberFixture.createHyena();
        final String token = getAccessTokenBySocialId(memberHyena.getSocialId().getValue());
        final Notification notification = NotificationFixture.create(memberHyena, notificationReferencedId(1L));

        final Member spyMember = spy(memberHyena);
        when(spyMember.getId()).thenReturn(1L);

        final Notification spyNotification = spy(notification);
        when(spyNotification.getId()).thenReturn(1L);
        doReturn(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .when(spyNotification)
                .getCreatedAt();

        // when
        when(notificationQueryService.readNotificationsByMemberId(anyLong(), anyInt())).thenReturn(List.of(spyNotification));
        when(oauthMemberCommandRepository.findBySocialId(any())).thenReturn(Optional.of(spyMember));

        // then
        mockMvc.perform(get("/api/v1/notifications")
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
                                fieldWithPath("data.[].notificationId").type(NUMBER).description("알림 식별자값(id)"),
                                fieldWithPath("data.[].title").type(STRING).description("알림 제목"),
                                fieldWithPath("data.[].message").type(STRING).description("알림 내용"),
                                fieldWithPath("data.[].notificationType").type(STRING).description("알림 타입 (with referencedId)"),
                                fieldWithPath("data.[].referencedId").type(NUMBER).description("알림 연관된 식별자값"),
                                fieldWithPath("data.[].isRead").type(BOOLEAN).description("알림 읽음 여부"),
                                fieldWithPath("data.[].createdAt").type(STRING).description("알림 생성 시간")
                        ))
                );
    }
}
