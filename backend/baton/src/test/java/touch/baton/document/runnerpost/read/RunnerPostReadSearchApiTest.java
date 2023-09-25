package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.RunnerPostReadController;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponses;
import touch.baton.domain.runnerpost.service.RunnerPostReadService;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

@WebMvcTest(RunnerPostReadController.class)
class RunnerPostReadSearchApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostReadService runnerPostReadService;

    @BeforeEach
    void setUp() {
        final RunnerPostReadController runnerPostReadController = new RunnerPostReadController(runnerPostReadService);
        restdocsSetUp(runnerPostReadController);
    }

    @DisplayName("태그 이름과 리뷰 상태를 조건으로 러너 게시글 페이징 조회 API")
    @Test
    void readRunnerPostsByTagNamesAndReviewStatus() throws Exception {
        // given
        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena());

        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final Tag javaTag = TagFixture.create(tagName("자바"));
        final Tag springTag = TagFixture.create(tagName("스프링"));
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline, List.of(javaTag, springTag));

        final RunnerPost spyRunnerPost = spy(runnerPost);
        given(spyRunnerPost.getId()).willReturn(1L);

        // when
        final RunnerPostResponse.Simple runnerPostResponse = RunnerPostResponse.Simple.from(spyRunnerPost, 0L);
        final RunnerPostResponses.Simple runnerPostResponses = RunnerPostResponses.Simple.from(List.of(runnerPostResponse));
        when(runnerPostReadService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(anyString(), anyLong(), anyInt(), any(ReviewStatus.class)))
                .thenReturn(runnerPostResponses);

        // then
        mockMvc.perform(get("/api/v1/posts/runner")
                        .queryParam("tagName", javaTag.getTagName().getValue())
                        .queryParam("cursor", String.valueOf(1000L))
                        .queryParam("limit", String.valueOf(10))
                        .queryParam("reviewStatus", ReviewStatus.NOT_STARTED.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("cursor").description("(Optional) 이전 페이지 마지막 게시글 식별자값(id)"),
                                parameterWithName("limit").description("페이지 사이즈"),
                                parameterWithName("reviewStatus").description("(Optional) 리뷰 상태"),
                                parameterWithName("tagName").description("(Optional) 태그 이름")
                        ),
                        responseFields(
                                fieldWithPath("data.[].runnerPostId").type(NUMBER).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("data.[].title").type(STRING).description("러너 게시글의 제목"),
                                fieldWithPath("data.[].deadline").type(STRING).description("러너 게시글의 마감 기한"),
                                fieldWithPath("data.[].watchedCount").type(NUMBER).description("러너 게시글의 조회수"),
                                fieldWithPath("data.[].applicantCount").type(NUMBER).description("러너 게시글에 신청한 서포터 수"),
                                fieldWithPath("data.[].reviewStatus").type(STRING).description("러너 게시글의 리뷰 상태"),
                                fieldWithPath("data.[].runnerProfile.name").type(STRING).description("러너 게시글의 러너 프로필 이름"),
                                fieldWithPath("data.[].runnerProfile.imageUrl").type(STRING).description("러너 게시글의 러너 프로필 이미지"),
                                fieldWithPath("data.[].tags.[]").type(ARRAY).description("러너 게시글의 태그 목록")
                        ))
                );
    }
}
