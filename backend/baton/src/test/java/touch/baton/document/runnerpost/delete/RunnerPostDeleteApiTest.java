package touch.baton.document.runnerpost.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.runnerpost.command.controller.RunnerPostCommandController;
import touch.baton.tobe.domain.runnerpost.command.service.RunnerPostCommandService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

@WebMvcTest(RunnerPostCommandController.class)
public class RunnerPostDeleteApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostCommandService runnerPostCommandService;

    @BeforeEach
    void setUp() {
        final RunnerPostCommandController runnerPostCommandController = new RunnerPostCommandController(runnerPostCommandService);
        restdocsSetUp(runnerPostCommandController);
    }

    @DisplayName("러너 게시글 삭제 API")
    @Test
    void deleteByRunnerPostId() throws Exception {
        // given
        final String socialId = "ditooSocialId";
        final Runner runner = RunnerFixture.createRunner(MemberFixture.createWithSocialId(socialId));
        final String token = getAccessTokenBySocialId(socialId);
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost spyRunnerPost = spy(runnerPost);

        // when
        when(oauthRunnerCommandRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(runner));
        when(spyRunnerPost.getId()).thenReturn(1L);

        // then
        mockMvc.perform(delete("/api/v1/posts/runner/{runnerPostId}", 1L)
                        .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNoContent())
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        pathParameters(
                                parameterWithName("runnerPostId").description("러너 게시글 식별자값")
                        )
                ));
    }
}
