package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Member;
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
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostCountWithLoginedSupporterApiTest extends RestdocsConfig {

    @DisplayName("로그인한 서포터와 관련된 러너 게시글 개수 조회 API")
    @Test
    void countRunnerPostByLoginedSupporterAndReviewStatus() throws Exception {
        // given
        final String socialId = "ditooSocialId";
        final Member loginedMember = MemberFixture.createWithSocialId(socialId);
        final Supporter loginedSupporter = SupporterFixture.create(loginedMember);
        final String token = getAccessTokenBySocialId(socialId);

        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena());

        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline);
        SupporterRunnerPostFixture.create(runnerPost, loginedSupporter);
        runnerPost.assignSupporter(loginedSupporter);

        final Supporter spyLoginedSupporter = spy(loginedSupporter);
        given(oauthSupporterCommandRepository.joinByMemberSocialId(any())).willReturn(Optional.ofNullable(spyLoginedSupporter));

        // when
        final long expectedCount = 1L;
        when(runnerPostQueryService.countRunnerPostBySupporterIdAndReviewStatus(eq(1L), eq(ReviewStatus.NOT_STARTED)))
                .thenReturn(expectedCount);

        // then
        mockMvc.perform(get("/api/v1/posts/runner/me/supporter/count")
                        .header(AUTHORIZATION, "Bearer " + token)
                        .queryParam("reviewStatus", ReviewStatus.NOT_STARTED.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        queryParameters(
                                parameterWithName("reviewStatus").description("리뷰 상태")
                        ),
                        responseFields(
                                fieldWithPath("count").type(NUMBER).optional().description("게시글 개수")
                        ))
                );
    }
}
