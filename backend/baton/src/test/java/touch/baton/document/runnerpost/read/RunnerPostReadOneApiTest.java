package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.runnerpost.command.vo.Deadline;
import touch.baton.tobe.domain.runnerpost.query.controller.RunnerPostQueryController;
import touch.baton.tobe.domain.runnerpost.query.service.RunnerPostQueryService;
import touch.baton.tobe.domain.tag.command.Tag;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.spy;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

@WebMvcTest(RunnerPostQueryController.class)
class RunnerPostReadOneApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostQueryService runnerPostQueryService;

    @BeforeEach
    void setUp() {
        final RunnerPostQueryController runnerPostQueryController = new RunnerPostQueryController(runnerPostQueryService);
        restdocsSetUp(runnerPostQueryController);
    }

    @DisplayName("러너 게시글 상세 조회 API")
    @Test
    void readByRunnerPostId() throws Exception {
        // given
        final Member memberHyena = MemberFixture.createHyena();
        final Runner runnerHyena = RunnerFixture.createRunner(memberHyena);

        final Deadline deadline = deadline(now().plusHours(100));
        final Tag javaTag = TagFixture.create(tagName("자바"));

        // when
        final Member spyMemberHyena = spy(memberHyena);
        final Runner spyRunnerHyena = spy(runnerHyena);
        when(spyRunnerHyena.getId()).thenReturn(1L);
        when(spyMemberHyena.getId()).thenReturn(1L);

        final RunnerPost runnerPost = RunnerPostFixture.create(spyRunnerHyena, deadline, List.of(javaTag));
        final RunnerPost spyRunnerPost = spy(runnerPost);
        when(spyRunnerPost.getId()).thenReturn(1L);

        when(runnerPostQueryService.readByRunnerPostId(any()))
                .thenReturn(spyRunnerPost);
        when(runnerPostQueryService.readCountByRunnerPostId(any()))
                .thenReturn(3L);

        final String token = getAccessTokenBySocialId(memberHyena.getSocialId().getValue());

        when(oauthMemberCommandRepository.findBySocialId(any()))
                .thenReturn(Optional.ofNullable(memberHyena));

        // then
        mockMvc.perform(get("/api/v1/posts/runner/{runnerPostId}", spyRunnerPost.getId())
                        .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT (필수값 x)")
                        ),
                        pathParameters(
                                parameterWithName("runnerPostId").description("러너 게시글 식별자값")
                        ),
                        responseFields(
                                fieldWithPath("runnerPostId").type(NUMBER).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("title").type(STRING).description("러너 게시글 제목"),
                                fieldWithPath("implementedContents").type(STRING).description("구현 내용"),
                                fieldWithPath("curiousContents").type(STRING).description("궁금한 점"),
                                fieldWithPath("postscriptContents").type(STRING).description("참고 사항"),
                                fieldWithPath("deadline").type(STRING).description("러너 게시글 마감 기한"),
                                fieldWithPath("isOwner").type(BOOLEAN).description("러너 게시글 주인 여부"),
                                fieldWithPath("isApplied").type(BOOLEAN).description("로그인한 서포터 리뷰 지원 여부"),
                                fieldWithPath("applicantCount").type(NUMBER).description("러너 게시글 서포터 지원자수"),
                                fieldWithPath("watchedCount").type(NUMBER).description("러너 게시글 조회수"),
                                fieldWithPath("reviewStatus").type(STRING).description("러너 게시글 리뷰 상태"),
                                fieldWithPath("pullRequestUrl").type(STRING).description("러너 게시글 PR URL"),
                                fieldWithPath("tags").type(ARRAY).description("러너 게시글 태그 목록"),
                                fieldWithPath("runnerProfile.runnerId").type(NUMBER).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("runnerProfile.name").type(STRING).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("runnerProfile.company").type(STRING).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("runnerProfile.imageUrl").type(STRING).description("러너 게시글 식별자값(id)")
                        ))
                );
    }
}
