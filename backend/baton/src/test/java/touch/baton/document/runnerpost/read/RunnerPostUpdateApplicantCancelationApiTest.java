package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.RunnerPostController;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.CompanyFixture.company;
import static touch.baton.fixture.vo.GithubUrlFixture.githubUrl;
import static touch.baton.fixture.vo.ImageUrlFixture.imageUrl;
import static touch.baton.fixture.vo.MemberNameFixture.memberName;
import static touch.baton.fixture.vo.OauthIdFixture.oauthId;
import static touch.baton.fixture.vo.SocialIdFixture.socialId;

@WebMvcTest(RunnerPostController.class)
public class RunnerPostUpdateApplicantCancelationApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        final RunnerPostController runnerPostController = new RunnerPostController(runnerPostService);
        restdocsSetUp(runnerPostController);
    }

    @DisplayName("러너 게시글에 리뷰 제안한 서포터가 리뷰 제안 철회 API")
    @Test
    void updateSupporterCancelRunnerPost() throws Exception {
        // given
        final String socialId = "ditooSocialId";
        final Member member = MemberFixture.create(
                memberName("디투"),
                socialId(socialId),
                oauthId("abcd"),
                githubUrl("naver.com"),
                company("우아한테크코스"),
                imageUrl("profile.jpg")
        );
        final Supporter supporter = SupporterFixture.create(member);
        final String token = getAccessTokenBySocialId(socialId);

        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, supporter, new Deadline(LocalDateTime.now().plusHours(10)));
        final RunnerPost spyRunnerPost = spy(runnerPost);

        // when
        when(spyRunnerPost.getId()).thenReturn(1L);
        when(oauthSupporterRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(supporter));
        runnerPostService.deleteSupporterRunnerPost(any(), eq(1L));

        // then
        mockMvc.perform(patch("/api/v1/posts/runner/{runnerPostId}/cancelation", 1L)
                        .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Location", "/api/v1/posts/runner/1"))
                .andDo(restDocs.document(
                        requestHeaders(
                                headerWithName(AUTHORIZATION).description("Bearer JWT")
                        ),
                        pathParameters(
                                parameterWithName("runnerPostId").description("러너 게시글 식별자값")
                        ),
                        responseHeaders(
                                headerWithName(LOCATION).description("redirect uri")
                        )
                ));
    }
}
