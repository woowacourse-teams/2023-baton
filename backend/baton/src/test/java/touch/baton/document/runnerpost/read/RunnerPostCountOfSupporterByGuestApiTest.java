package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostCountOfSupporterByGuestApiTest extends RestdocsConfig {

    @DisplayName("서포터가 완료한 러너 게시글 개수 조회 API")
    @Test
    void countRunnerPostBySupporterIdAndReviewStatus() throws Exception {
        // given
        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena());
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline);

        final Supporter supporter = SupporterFixture.create(MemberFixture.createDitoo());
        SupporterRunnerPostFixture.create(runnerPost, supporter);
        runnerPost.assignSupporter(supporter);

        final Supporter spySupporter = spy(supporter);
        given(spySupporter.getId()).willReturn(1L);

        // when
        final long expectedCount = 1L;
        when(runnerPostQueryService.countRunnerPostBySupporterIdAndReviewStatus(eq(1L), eq(ReviewStatus.DONE)))
                .thenReturn(expectedCount);

        // then
        mockMvc.perform(get("/api/v1/posts/runner/search/count")
                        .queryParam(
                                "supporterId", String.valueOf(spySupporter.getId())
                        ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("supporterId").description("서포터 식별자 값")
                        ),
                        responseFields(
                                fieldWithPath("count").type(NUMBER).optional().description("게시글 개수")
                        ))
                );
    }
}
