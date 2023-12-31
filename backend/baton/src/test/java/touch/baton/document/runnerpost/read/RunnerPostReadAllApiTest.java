package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.RunnerPostController;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.spy;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

@WebMvcTest(RunnerPostController.class)
class RunnerPostReadAllApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        final RunnerPostController runnerPostController = new RunnerPostController(runnerPostService);
        restdocsSetUp(runnerPostController);
    }

    @DisplayName("러너 게시글 전체 조회 API")
    @Test
    void readRunnerPostsByReviewStatus() throws Exception {
        // given
        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena());
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final Tag javaTag = TagFixture.create(tagName("자바"));
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline, List.of(javaTag));
        final RunnerPost spyRunnerPost = spy(runnerPost);
        final ReviewStatus reviewStatus = ReviewStatus.IN_PROGRESS;
        given(spyRunnerPost.getId()).willReturn(1L);

        // when
        final List<RunnerPost> runnerPosts = List.of(spyRunnerPost);
        final PageRequest pageOne = PageRequest.of(1, 10);
        final PageImpl<RunnerPost> pageRunnerPosts = new PageImpl<>(runnerPosts, pageOne, runnerPosts.size());
        when(spyRunnerPost.getReviewStatus()).thenReturn(reviewStatus);
        when(runnerPostService.readRunnerPostsByReviewStatus(any(), any())).thenReturn(pageRunnerPosts);
        when(runnerPostService.readCountsByRunnerPostIds(anyList())).thenReturn(List.of(1L));

        // then
        mockMvc.perform(get("/api/v1/posts/runner")
                        .queryParam("size", String.valueOf(pageOne.getPageSize()))
                        .queryParam("page", String.valueOf(pageOne.getPageNumber()))
                        .queryParam("reviewStatus", String.valueOf(reviewStatus)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("reviewStatus").description("리뷰 상태")
                        ),
                        responseFields(
                                fieldWithPath("data.[].runnerPostId").type(NUMBER).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("data.[].title").type(STRING).description("러너 게시글의 제목"),
                                fieldWithPath("data.[].deadline").type(STRING).description("러너 게시글의 마감 기한"),
                                fieldWithPath("data.[].watchedCount").type(NUMBER).description("러너 게시글의 조회수"),
                                fieldWithPath("data.[].reviewStatus").type(STRING).description("러너 게시글의 리뷰 상태"),
                                fieldWithPath("data.[].runnerProfile.name").type(STRING).description("러너 게시글의 러너 프로필 이름"),
                                fieldWithPath("data.[].runnerProfile.imageUrl").type(STRING).description("러너 게시글의 러너 프로필 이미지"),
                                fieldWithPath("data.[].tags.[]").type(ARRAY).description("러너 게시글의 태그 목록"),
                                fieldWithPath("data.[].applicantCount").type(NUMBER).description("러너 게시글에 신청한 서포터 수"),
                                fieldWithPath("pageInfo.isFirst").type(BOOLEAN).description("첫 번째 페이지인지"),
                                fieldWithPath("pageInfo.isLast").type(BOOLEAN).description("마지막 페이지인지"),
                                fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지가 있는지"),
                                fieldWithPath("pageInfo.totalPages").type(NUMBER).description("총 페이지 수"),
                                fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 데이터 수"),
                                fieldWithPath("pageInfo.currentPage").type(NUMBER).description("현재 페이지"),
                                fieldWithPath("pageInfo.currentSize").type(NUMBER).description("현재 페이지 데이터 수")
                        ))
                );
    }

    @DisplayName("러너와 연관된 러너 게시글 페이징 조회 API")
    @Test
    void readRunnerMyPage() throws Exception {
        // given
        final Runner runnerJudy = RunnerFixture.createRunner(MemberFixture.createJudy());
        final String token = getAccessTokenBySocialId(runnerJudy.getMember().getSocialId().getValue());

        when(oauthRunnerRepository.joinByMemberSocialId(notNull()))
                .thenReturn(Optional.ofNullable(runnerJudy));

        final Tag javaTag = TagFixture.create(tagName("자바"));
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final RunnerPost runnerPost = RunnerPostFixture.create(runnerJudy, deadline, List.of(javaTag));

        // when
        final RunnerPost spyRunnerPost = spy(runnerPost);
        when(spyRunnerPost.getId()).thenReturn(1L);

        final List<RunnerPost> runnerPosts = List.of(spyRunnerPost);
        final PageRequest pageOne = PageRequest.of(1, 10);
        final PageImpl<RunnerPost> pageRunnerPosts = new PageImpl<>(runnerPosts, pageOne, runnerPosts.size());
        when(runnerPostService.readRunnerPostsByRunnerIdAndReviewStatus(any(), any(), any()))
                .thenReturn(pageRunnerPosts);
        when(runnerPostService.readCountsByRunnerPostIds(anyList()))
                .thenReturn(List.of(0L));

        // then
        mockMvc.perform(get("/api/v1/posts/runner/me/runner")
                        .header(AUTHORIZATION, "Bearer " + token)
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                        .queryParam("size", String.valueOf(pageOne.getPageSize()))
                        .queryParam("page", String.valueOf(pageOne.getPageNumber()))
                        .queryParam("reviewStatus", ReviewStatus.IN_PROGRESS.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        queryParameters(
                                parameterWithName("size").description("페이지 사이즈"),
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("reviewStatus").description("리뷰 상태")
                        ),
                        responseFields(
                                fieldWithPath("data.[].runnerPostId").type(NUMBER).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("data.[].supporterId").type(NUMBER).optional().description("서포터 식별자값(id)은 null 일 수 있다"),
                                fieldWithPath("data.[].title").type(STRING).description("러너 게시글 제목"),
                                fieldWithPath("data.[].deadline").type(STRING).description("러너 게시글의 마감 기한"),
                                fieldWithPath("data.[].tags").type(ARRAY).description("러너 게시글 태그 목록"),
                                fieldWithPath("data.[].watchedCount").type(NUMBER).description("러너 게시글의 조회수"),
                                fieldWithPath("data.[].applicantCount").type(NUMBER).description("러너 게시글에 신청한 서포터 수"),
                                fieldWithPath("data.[].reviewStatus").type(STRING).description("러너 게시글 리뷰 상태"),
                                fieldWithPath("data.[].isReviewed").type(BOOLEAN).description("러너 게시글을 리뷰해준 서포터에 대한 피드백 유무"),
                                fieldWithPath("pageInfo.isFirst").type(BOOLEAN).description("첫 번째 페이지인지"),
                                fieldWithPath("pageInfo.isLast").type(BOOLEAN).description("마지막 페이지 인지"),
                                fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지가 있는지"),
                                fieldWithPath("pageInfo.totalPages").type(NUMBER).description("총 페이지 수"),
                                fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 데이터 수"),
                                fieldWithPath("pageInfo.currentPage").type(NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("pageInfo.currentSize").type(NUMBER).description("현재 페이지 데이터 수")
                        ))
                );
    }
}
