package touch.baton.document.github;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.controller.MemberBranchController;
import touch.baton.tobe.domain.member.command.service.GithubBranchManageable;
import touch.baton.tobe.domain.member.command.service.dto.GithubRepoNameRequest;

import java.util.Optional;

import static org.apache.http.HttpHeaders.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberBranchController.class)
class GithubBranchApiTest extends RestdocsConfig {

    @MockBean
    private GithubBranchManageable githubBranchManageable;

    @BeforeEach
    void setup() {
        restdocsSetUp(new MemberBranchController(githubBranchManageable));
    }

    @DisplayName("깃허브 레포 브랜치 생성 API")
    @Test
    void createMemberBranch() throws Exception {
        // given
        final String socialId = "hongSile";
        final Member member = MemberFixture.createWithSocialId(socialId);
        final String accessToken = getAccessTokenBySocialId(socialId);
        final GithubRepoNameRequest request = new GithubRepoNameRequest("drunken-ditoo");

        // when
        when(oauthMemberCommandRepository.findBySocialId(any())).thenReturn(Optional.ofNullable(member));
        doNothing().when(githubBranchManageable).createBranch(eq(socialId), anyString());

        // then
        mockMvc.perform(post("/api/v1/branch")
                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/api/v1/profile/me"))
                .andDo(restDocs.document(
                        requestHeaders(headerWithName(AUTHORIZATION).description("Bearer Token"),
                                headerWithName(CONTENT_TYPE).description(APPLICATION_JSON_VALUE)),
                        responseHeaders(headerWithName(LOCATION).description("Redirect URI"))
                ));
    }
}
