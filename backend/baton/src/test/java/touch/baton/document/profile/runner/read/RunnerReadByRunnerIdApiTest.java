package touch.baton.document.profile.runner.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.query.controller.RunnerQueryController;
import touch.baton.tobe.domain.member.query.service.RunnerQueryService;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;

import static javax.swing.text.html.parser.DTDConstants.NUMBER;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunnerQueryController.class)
public class RunnerReadByRunnerIdApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @MockBean
    private RunnerQueryService runnerQueryService;

    @BeforeEach
    void setUp() {
        final RunnerQueryController runnerQueryController = new RunnerQueryController(runnerPostService, runnerQueryService);
        restdocsSetUp(runnerQueryController);
    }

    @DisplayName("러너 프로필 상세 조회 API")
    @Test
    void readRunnerProfile() throws Exception {
        // given
        final Member ethan = MemberFixture.createEthan();
        final TechnicalTag javaTag = TechnicalTagFixture.createJava();
        final TechnicalTag reactTag = TechnicalTagFixture.createReact();
        final Runner runner = RunnerFixture.createRunner(ethan, List.of(javaTag, reactTag));
        final Runner spyRunner = spy(runner);

        // when
        when(spyRunner.getId()).thenReturn(1L);
        when(runnerQueryService.readByRunnerId(anyLong())).thenReturn(spyRunner);

        // then
        mockMvc.perform(get("/api/v1/profile/runner/{runnerId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("runnerId").description("러너 식별자값")
                        ),
                        responseFields(
                                fieldWithPath("runnerId").type(NUMBER).description("러너 식별자값"),
                                fieldWithPath("name").type(STRING).description("러너 이름"),
                                fieldWithPath("imageUrl").type(STRING).description("사용자 이미지"),
                                fieldWithPath("githubUrl").type(STRING).description("깃허브 프로필 url"),
                                fieldWithPath("introduction").type(STRING).description("소개"),
                                fieldWithPath("company").type(STRING).description("소속"),
                                fieldWithPath("technicalTags").type(ARRAY).description("기술 스택")
                        )
                ));
    }
}
