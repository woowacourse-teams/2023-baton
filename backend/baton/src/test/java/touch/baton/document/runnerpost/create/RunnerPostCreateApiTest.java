package touch.baton.document.runnerpost.create;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostCreateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpHeaders.LOCATION;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RunnerPostCreateApiTest extends RestdocsConfig {

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
        when(runnerPostCommandService.createRunnerPost(any(Runner.class), any(RunnerPostCreateRequest.class))).thenReturn(1L);
        when(oauthRunnerCommandRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(runner));

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
