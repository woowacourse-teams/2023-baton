package touch.baton.document.profile.runner.update;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.controller.RunnerProfileController;
import touch.baton.domain.runner.service.RunnerService;
import touch.baton.domain.runner.service.dto.RunnerUpdateRequest;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.supporter.controller.SupporterProfileController;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunnerProfileController.class)
public class RunnerProfileUpdateApiTest extends RestdocsConfig {

    @MockBean
    private RunnerService runnerService;

    @MockBean
    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        final RunnerProfileController runnerProfileController = new RunnerProfileController(runnerPostService, runnerService);
        restdocsSetUp(runnerProfileController);
    }

    @DisplayName("러너 프로필 수정 API")
    @Test
    void updateRunnerProfile() throws Exception {
        // given
        final RunnerUpdateRequest request = new RunnerUpdateRequest("주디", "우아한테크코스", "주디입니다.", List.of("spring", "java"));
        final String requestBody = objectMapper.writeValueAsString(request);
        final String socialId = "judySocicalId";
        final Member judyMember = MemberFixture.createJudy();
        final Runner judyRunner = RunnerFixture.createRunner(judyMember);
        final String token = getAccessTokenBySocialId(socialId);

        // when
        when(oauthRunnerRepository.joinByMemberSocialId(any()))
                .thenReturn(Optional.ofNullable(judyRunner));

        // then
        mockMvc.perform(patch("/api/v1/profile/runner/me")
                        .header(AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "/api/v1/profile/runner/me"))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT"),
                                headerWithName(CONTENT_TYPE).description("application/json")
                        ),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("변경할 이름"),
                                fieldWithPath("company").type(STRING).description("변경할 소속"),
                                fieldWithPath("introduction").type(STRING).description("변경할 소개글"),
                                fieldWithPath("technicalTags.[]").type(ARRAY).description("변경할 기술 태그 목록")
                        ),
                        responseHeaders(headerWithName(LOCATION).description("redirect uri"))
                ))
                .andDo(print());
    }
}
