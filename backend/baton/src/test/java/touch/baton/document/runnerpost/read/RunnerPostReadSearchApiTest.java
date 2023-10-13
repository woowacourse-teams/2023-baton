package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.common.request.PageParams;
import touch.baton.domain.common.response.PageResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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

class RunnerPostReadSearchApiTest extends RestdocsConfig {

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
        final List<RunnerPostResponse.Simple> responses = List.of(RunnerPostResponse.Simple.of(
                spyRunnerPost,
                0L,
                List.of(RunnerPostTagFixture.create(spyRunnerPost, javaTag), RunnerPostTagFixture.create(spyRunnerPost, springTag))
        ));
        final PageResponse<RunnerPostResponse.Simple> pageResponse = PageResponse.of(responses, new PageParams(2L, 10));
        when(runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(anyString(), any(PageParams.class), any(ReviewStatus.class)))
                .thenReturn(pageResponse);

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
                                fieldWithPath("data.[].tags.[]").type(ARRAY).description("러너 게시글의 태그 목록"),
                                fieldWithPath("pageInfo.isLast").type(BOOLEAN).description("마지막 페이지 여부"),
                                fieldWithPath("pageInfo.nextCursor").type(NUMBER).optional().description("다음 커서")
                        ))
                );
    }
}
