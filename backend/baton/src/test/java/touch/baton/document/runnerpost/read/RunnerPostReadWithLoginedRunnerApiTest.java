package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.common.request.PageParams;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;
import touch.baton.domain.tag.command.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.JsonFieldType.VARIES;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class RunnerPostReadWithLoginedRunnerApiTest extends RestdocsConfig {

    @DisplayName("로그인한 러너가 작성한 러너 게시글 페이징 조회 API")
    @Test
    void readRunnerPostByLoginedRunnerAndReviewStatus() throws Exception {
        // given
        final String socialId = "ditooSocialId";
        final Member loginedMember = MemberFixture.createWithSocialId(socialId);
        final Runner loginedRunner = RunnerFixture.createRunner(loginedMember);
        final String token = getAccessTokenBySocialId(socialId);

        final Tag javaTag = TagFixture.create(tagName("자바"));
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final RunnerPost runnerPost = RunnerPostFixture.create(loginedRunner, deadline, List.of(javaTag));

        final Runner spyLoginedRunner = spy(loginedRunner);
        given(oauthRunnerCommandRepository.joinByMemberSocialId(any())).willReturn(Optional.ofNullable(spyLoginedRunner));
        given(Objects.requireNonNull(spyLoginedRunner).getId()).willReturn(1L);
        final RunnerPost spyRunnerPost = spy(runnerPost);
        given(spyRunnerPost.getId()).willReturn(1L);

        // when
        final List<RunnerPostResponse.SimpleByRunner> responses = List.of(RunnerPostResponse.SimpleByRunner.of(
                spyRunnerPost,
                0L,
                List.of(RunnerPostTagFixture.create(spyRunnerPost, javaTag))
        ));
        final PageResponse<RunnerPostResponse.SimpleByRunner> pageResponse = PageResponse.of(responses, new PageParams(2L, 10));
        when(runnerPostQueryService.pageRunnerPostByRunnerIdAndReviewStatus(any(PageParams.class), eq(1L), eq(ReviewStatus.NOT_STARTED)))
                .thenReturn(pageResponse);

        // then
        mockMvc.perform(get("/api/v1/posts/runner/me/runner")
                        .header(AUTHORIZATION, "Bearer " + token)
                        .queryParam("cursor", String.valueOf(1000L))
                        .queryParam("limit", String.valueOf(10))
                        .queryParam("reviewStatus", ReviewStatus.NOT_STARTED.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        queryParameters(
                                parameterWithName("cursor").description("(Optional) 이전 페이지 마지막 게시글 식별자값(id)"),
                                parameterWithName("limit").description("페이지 사이즈"),
                                parameterWithName("reviewStatus").description("(Optional) 리뷰 상태")
                        ),
                        responseFields(
                                fieldWithPath("data.[].runnerPostId").type(NUMBER).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("data.[].title").type(STRING).description("러너 게시글의 제목"),
                                fieldWithPath("data.[].deadline").type(STRING).description("러너 게시글의 마감 기한"),
                                fieldWithPath("data.[].watchedCount").type(NUMBER).description("러너 게시글의 조회수"),
                                fieldWithPath("data.[].applicantCount").type(NUMBER).description("러너 게시글에 신청한 서포터 수"),
                                fieldWithPath("data.[].reviewStatus").type(STRING).description("러너 게시글의 리뷰 상태"),
                                fieldWithPath("data.[].isReviewed").type(BOOLEAN).description("러너 게시글의 리뷰 완료 여부"),
                                fieldWithPath("data.[].supporterId").type(VARIES)
                                        .optional()
                                        .description("서포터 id (서포터가 존재할 때 NUMBER, 아닌 경우에 NULL)"),
                                fieldWithPath("data.[].tags.[]").type(ARRAY).description("러너 게시글의 태그 목록"),
                                fieldWithPath("pageInfo.isLast").type(BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("pageInfo.nextCursor").type(NUMBER).optional().description("다음 커서")
                        ))
                );
    }
}
