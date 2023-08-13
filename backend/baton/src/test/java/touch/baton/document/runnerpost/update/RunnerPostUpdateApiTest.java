package touch.baton.document.runnerpost.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runnerpost.controller.RunnerPostController;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunnerPostController.class)
public class RunnerPostUpdateApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        restdocsSetUp(new RunnerPostController(runnerPostService));
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
        willDoNothing().given(runnerPostService).updateRunnerPostReviewStatusDone(anyLong(), any(Supporter.class));
        when(oauthSupporterRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(supporter));

        // then
        mockMvc.perform(patch("/api/v1/posts/runner/{runnerPostId}/done", 1L)
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isNoContent())
                .andExpect(redirectedUrl("/api/v1/posts/runner/1"))
                .andDo(restDocs.document(
                        requestHeaders(headerWithName(AUTHORIZATION).description("Bearer TOKEN")),
                        responseHeaders(headerWithName(LOCATION).description("Redirect Uri"))
                ));
    }
}
