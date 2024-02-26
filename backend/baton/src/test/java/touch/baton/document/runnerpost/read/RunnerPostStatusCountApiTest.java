package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;

import static org.mockito.BDDMockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RunnerPostStatusCountApiTest extends RestdocsConfig {

    @DisplayName("게시글 상태 별 총 개수 반환 API 구현")
    @Test
    void countAllRunnerPostByReviewStatus() throws Exception {
        // when
        when(runnerPostQueryService.countAllRunnerPostByReviewStatus())
                .thenReturn(new RunnerPostResponse.StatusCount(1L, 2L, 3L, 4L));

        // then
        mockMvc.perform(get("/api/v1/posts/runner/count"))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("notStarted").type(JsonFieldType.NUMBER).description("리뷰_대기중_게시글_총_수"),
                                fieldWithPath("inProgress").type(JsonFieldType.NUMBER).description("리뷰_진행중_게시글_총_수"),
                                fieldWithPath("done").type(JsonFieldType.NUMBER).description("리뷰_완료_게시글_총_수"),
                                fieldWithPath("overdue").type(JsonFieldType.NUMBER).description("기간_만료_게시글_총_수")
                        ))
                );
    }
}
