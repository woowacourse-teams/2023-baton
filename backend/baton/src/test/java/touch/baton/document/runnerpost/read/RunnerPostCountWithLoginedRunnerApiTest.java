package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.tag.command.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;
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
import static touch.baton.fixture.vo.TagNameFixture.tagName;

public class RunnerPostCountWithLoginedRunnerApiTest extends RestdocsConfig {

    @DisplayName("로그인한 러너와 관련된 러너 게시글 개수 조회 API")
    @Test
    void countRunnerPostByLoginedRunnerAndReviewStatus() throws Exception {
        // given
        final String socialId = "ditooSocialId";
        final Member loginedMember = MemberFixture.createWithSocialId(socialId);
        final Runner loginedRunner = RunnerFixture.createRunner(loginedMember);
        final String token = getAccessTokenBySocialId(socialId);

        final Tag javaTag = TagFixture.create(tagName("자바"));
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        RunnerPostFixture.create(loginedRunner, deadline, List.of(javaTag));
        final Runner spyLoginedRunner = spy(loginedRunner);
        given(oauthRunnerCommandRepository.joinByMemberSocialId(any())).willReturn(Optional.ofNullable(spyLoginedRunner));

        // when
        final long expectedCount = 1L;
        when(runnerPostQueryService.countRunnerPostByRunnerIdAndReviewStatus(eq(1L), eq(ReviewStatus.NOT_STARTED)))
                .thenReturn(expectedCount);

        // then
        mockMvc.perform(get("/api/v1/posts/runner/me/runner/count")
                        .header(AUTHORIZATION, "Bearer " + token)
                        .queryParam("reviewStatus", ReviewStatus.NOT_STARTED.name()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        queryParameters(
                                parameterWithName("reviewStatus").description("(Optional) 리뷰 상태")
                        ),
                        responseFields(
                                fieldWithPath("count").type(NUMBER).optional().description("게시글 개수")
                        ))
                );
    }
}
