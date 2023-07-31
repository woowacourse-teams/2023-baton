package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.service.RunnerService;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.RunnerPostController;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagCountFixture.tagCount;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

@WebMvcTest(RunnerPostController.class)
class RunnerPostReadApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @MockBean
    private RunnerService runnerService;

    @DisplayName("러너 게시글 전체 조회 API")
    @Test
    void readAllRunnerPosts() throws Exception {
        // given
        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena());
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final Tag javaTag = TagFixture.create(tagName("자바"), tagCount(10));
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline, List.of(javaTag));
        final RunnerPost spyRunnerPost = spy(runnerPost);

        // when
        given(spyRunnerPost.getId()).willReturn(1L);
        given(runnerPostService.readAllRunnerPosts()).willReturn(List.of(spyRunnerPost));

        // then
        mockMvc.perform(get("/api/v1/posts/runner"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("data.[].runnerPostId").type(NUMBER).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("data.[].title").type(STRING).description("러너 게시글의 제목"),
                                fieldWithPath("data.[].deadline").type(STRING).description("러너 게시글의 마감 기한"),
                                fieldWithPath("data.[].watchedCount").type(NUMBER).description("러너 게시글의 조회수"),
                                fieldWithPath("data.[].chattingCount").type(NUMBER).description("러너 게시글의 채팅수"),
                                fieldWithPath("data.[].reviewStatus").type(STRING).description("러너 게시글의 리뷰 상태"),
                                fieldWithPath("data.[].runnerProfile.name").type(STRING).description("러너 게시글의 러너 프로필 이름"),
                                fieldWithPath("data.[].runnerProfile.imageUrl").type(STRING).description("러너 게시글의 러너 프로필 이미지"),
                                fieldWithPath("data.[].tags.[]").type(ARRAY).description("러너 게시글의 태그 목록")
                        ))
                );
    }
}
