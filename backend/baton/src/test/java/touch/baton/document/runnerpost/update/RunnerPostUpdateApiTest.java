package touch.baton.document.runnerpost.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.controller.RunnerPostCommandController;
import touch.baton.domain.runnerpost.command.service.RunnerPostCommandService;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostUpdateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.Optional;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunnerPostCommandController.class)
public class RunnerPostUpdateApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostCommandService runnerPostCommandService;

    @BeforeEach
    void setUp() {
        restdocsSetUp(new RunnerPostCommandController(runnerPostCommandService));
    }

    @DisplayName("제안한 서포터 목록 중에서 서포터로 선택하는 API")
    @Test
    void updateRunnerPostSupporter() throws Exception {
        // given
        final String ditooSocialId = "helloToken";
        final String token = getAccessTokenBySocialId(ditooSocialId);
        final Member ditooMember = MemberFixture.createWithSocialId(ditooSocialId);
        final Runner ditooRunner = RunnerFixture.createRunner(ditooMember);

        final RunnerPostUpdateRequest.SelectSupporter request = new RunnerPostUpdateRequest.SelectSupporter(1L);

        // when
        willDoNothing().given(runnerPostCommandService).updateRunnerPostAppliedSupporter(any(Runner.class), anyLong(), any(RunnerPostUpdateRequest.SelectSupporter.class));
        when(oauthRunnerCommandRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(ditooRunner));

        // then
        mockMvc.perform(patch("/api/v1/posts/runner/{runnerPostId}/supporters", 1L)
                        .header(AUTHORIZATION, "Bearer " + token)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent())
                .andExpect(redirectedUrl("/api/v1/posts/runner/1"))
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("runnerPostId").description("러너 게시글 식별자값")),
                        requestHeaders(headerWithName(AUTHORIZATION).description("Bearer JWT"),
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON_VALUE)),
                        responseHeaders(headerWithName(LOCATION).description("Redirect URI"))
                )).andDo(print());
    }

    @DisplayName("서포터 리뷰 완료 API")
    @Test
    void updateRunnerPostReviewStatusDone() throws Exception {
        // given
        final String ditooSocialId = "hongSile";
        final Member memberDitoo = MemberFixture.createWithSocialId(ditooSocialId);
        final Supporter supporter = SupporterFixture.create(memberDitoo);
        final String accessToken = getAccessTokenBySocialId(ditooSocialId);

        // when
        willDoNothing().given(runnerPostCommandService).updateRunnerPostReviewStatusDone(anyLong(), any(Supporter.class));
        when(oauthSupporterCommandRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(supporter));

        // then
        mockMvc.perform(patch("/api/v1/posts/runner/{runnerPostId}/done", 1L)
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isNoContent())
                .andExpect(redirectedUrl("/api/v1/posts/runner/1"))
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("runnerPostId").description("러너 게시글 식별자값")),
                        requestHeaders(headerWithName(AUTHORIZATION).description("Bearer TOKEN")),
                        responseHeaders(headerWithName(LOCATION).description("Redirect Uri"))
                ));
    }
}
