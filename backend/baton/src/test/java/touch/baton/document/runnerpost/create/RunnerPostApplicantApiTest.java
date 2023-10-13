package touch.baton.document.runnerpost.create;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.domain.tag.command.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.TagFixture;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class RunnerPostApplicantApiTest extends RestdocsConfig {

    @DisplayName("Supporter 가 RunnerPost 에 리뷰를 제안한다.")
    @Test
    void createRunnerPostApplicant() throws Exception {
        // given
        final Runner runnerEthan = RunnerFixture.createRunner(MemberFixture.createEthan());
        final Supporter supporterHyena = SupporterFixture.create(MemberFixture.createHyena());

        final Deadline deadline = deadline(now().plusHours(100));
        final Tag javaTag = TagFixture.create(tagName("자바"));
        final RunnerPost runnerPost = RunnerPostFixture.create(runnerEthan, deadline, List.of(javaTag));

        // when
        final RunnerPost spyRunnerPost = spy(runnerPost);
        when(spyRunnerPost.getId()).thenReturn(1L);
        when(runnerPostCommandService.createRunnerPostApplicant(any(), any(), any())).thenReturn(1L);

        when(oauthSupporterCommandRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(supporterHyena));

        final String token = getAccessTokenBySocialId(supporterHyena.getMember().getSocialId().getValue());

        // then
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요, 서포터 헤나입니다.");

        mockMvc.perform(post("/api/v1/posts/runner/{runnerPostId}/application", spyRunnerPost.getId())
                        .header(AUTHORIZATION, "Bearer " + token)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/api/v1/posts/runner/" + spyRunnerPost.getId()))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT"),
                                headerWithName(CONTENT_TYPE).description("application/json")
                        ),
                        pathParameters(
                                parameterWithName("runnerPostId").description("러너 게시글 식별자값")
                        ),
                        requestFields(
                                fieldWithPath("message").type(STRING).description("서포터의 러너 게시글 리뷰 지원 메시지")
                        ),
                        responseHeaders(
                                headerWithName(LOCATION).description("redirect uri")
                        )
                ));
    }
}
