package touch.baton.document.runnerpost.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.controller.RunnerPostController;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.apache.http.HttpHeaders.*;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunnerPostController.class)
class RunnerPostCreateApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        restdocsSetUp(new RunnerPostController(runnerPostService));
    }

    @DisplayName("러너 게시글 등록 API")
    @Test
    void createRunnerPost() throws Exception {
        // given
        final String socialId = "hongSile";
        final Member member = MemberFixture.createWithSocialId(socialId);
        final Runner runner = RunnerFixture.createRunner(member);
        final String accessToken = getAccessTokenBySocialId(socialId);

        final RunnerPostCreateRequest request = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.now().plusDays(10),
                "12345".repeat(200),
                "궁금해 궁금해~",
                "참고 부탁해요."
        );

        // when
        when(runnerPostService.createRunnerPost(any(Runner.class), any(RunnerPostCreateRequest.class))).thenReturn(1L);
        when(oauthRunnerRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(runner));

        // then
        mockMvc.perform(post("/api/v1/posts/runner")
                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/v1/posts/runner/1"))
                .andDo(restDocs.document(
                        requestHeaders(headerWithName(AUTHORIZATION).description("Bearer Token"),
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON_VALUE)),
                        responseHeaders(headerWithName(LOCATION).description("Redirect URI"))
                ));
    }
}
