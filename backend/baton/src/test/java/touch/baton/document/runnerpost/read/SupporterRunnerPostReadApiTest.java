package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.RunnerPostController;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
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

@WebMvcTest(RunnerPostController.class)
class SupporterRunnerPostReadApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        final RunnerPostController runnerPostController = new RunnerPostController(runnerPostService);
        restdocsSetUp(runnerPostController);
    }

    @DisplayName("러너 게시글의 지원한 서포터 목록 조회 API")
    @Test
    void readSupporterRunnerPostsByRunnerPostId() throws Exception {
        // given
        final Member member = MemberFixture.createHyena();
        final Runner runner = RunnerFixture.createRunner(member);
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final Tag javaTag = TagFixture.create(tagName("자바"));
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline, List.of(javaTag));
        final RunnerPost spyRunnerPost = spy(runnerPost);

        final Supporter supporter = SupporterFixture.create(MemberFixture.createJudy());
        final Supporter spySupporter = spy(supporter);
        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(spyRunnerPost, spySupporter);
        final String token = getAccessTokenBySocialId(runner.getMember().getSocialId().getValue());

        // when
        given(spySupporter.getId()).willReturn(1L);
        given(spyRunnerPost.getId()).willReturn(1L);
        given(runnerPostService.readSupporterRunnerPostsByRunnerPostId(any(), any())).willReturn(List.of(supporterRunnerPost));
        when(oauthRunnerRepository.joinByMemberSocialId(notNull()))
                .thenReturn(Optional.ofNullable(runner));

        // then
        mockMvc.perform(get("/api/v1/posts/runner/{runnerPostId}/supporters", 1L)
                        .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        pathParameters(parameterWithName("runnerPostId").description("러너 게시글 식별자(id)")),
                        responseFields(
                                fieldWithPath("data.[].supporterId").type(NUMBER).description("서포터 러너 게시글 서포터의 식별자값(id)"),
                                fieldWithPath("data.[].name").type(STRING).description("서포터 러너 게시글의 서포터의 이름"),
                                fieldWithPath("data.[].company").type(STRING).description("서포터 러너 게시글의 서포터의 소속"),
                                fieldWithPath("data.[].reviewCount").type(NUMBER).description("서포터 러너 게시글의 서포터의 리뷰수"),
                                fieldWithPath("data.[].imageUrl").type(STRING).description("서포터 러너 게시글의 서포터의 이미지 주소"),
                                fieldWithPath("data.[].message").type(STRING).description("서포터 러너 게시글의 메세지"),
                                fieldWithPath("data.[].technicalTags.[]").type(ARRAY).description("서포터 러너 게시글의 서포터의 태그 목록")
                        ))
                );
    }
}
