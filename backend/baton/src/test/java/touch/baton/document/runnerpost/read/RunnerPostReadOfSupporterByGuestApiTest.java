package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponses;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.controller.RunnerPostQueryController;
import touch.baton.domain.runnerpost.query.service.RunnerPostQueryService;
import touch.baton.domain.runnerpost.query.service.dto.PageParams;
import touch.baton.domain.tag.command.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

@WebMvcTest(RunnerPostQueryController.class)
class RunnerPostReadOfSupporterByGuestApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostQueryService runnerPostQueryService;

    @BeforeEach
    void setUp() {
        final RunnerPostQueryController runnerPostQueryController = new RunnerPostQueryController(runnerPostQueryService);
        restdocsSetUp(runnerPostQueryController);
    }

    @DisplayName("서포터와 연관된 러너 게시글 페이징 조회 API")
    @Test
    void readRunnerPostBySupporterIdAndReviewStatus() throws Exception {
        // given
        final Runner runner = RunnerFixture.createRunner(MemberFixture.createDitoo());
        final Supporter supporter = SupporterFixture.create(MemberFixture.createHyena());

        final Tag javaTag = TagFixture.create(tagName("자바"));
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline, List.of(javaTag));
        runnerPost.assignSupporter(supporter);

        final Supporter spySupporter = spy(supporter);
        given(spySupporter.getId()).willReturn(1L);
        final RunnerPost spyRunnerPost = spy(runnerPost);
        given(spyRunnerPost.getId()).willReturn(1L);

        // when
        final RunnerPostResponses.Simple responses = RunnerPostResponses.Simple.from(List.of(
                RunnerPostResponse.Simple.from(spyRunnerPost, 0L)
        ));
        when(runnerPostQueryService.pageRunnerPostBySupporterIdAndReviewStatus(any(PageParams.class), anyLong(), any(ReviewStatus.class)))
                .thenReturn(responses);

        // then
        mockMvc.perform(get("/api/v1/posts/runner/search")
                        .queryParam("cursor", String.valueOf(1000L))
                        .queryParam("limit", String.valueOf(10))
                        .queryParam("supporterId", String.valueOf(spySupporter.getId()))
                        .queryParam("reviewStatus", ReviewStatus.IN_PROGRESS.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("cursor").description("(Optional) 이전 페이지 마지막 게시글 식별자값(id)"),
                                parameterWithName("limit").description("페이지 사이즈"),
                                parameterWithName("supporterId").description("서포터 식별자값"),
                                parameterWithName("reviewStatus").description("(Optional) 리뷰 상태")
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
