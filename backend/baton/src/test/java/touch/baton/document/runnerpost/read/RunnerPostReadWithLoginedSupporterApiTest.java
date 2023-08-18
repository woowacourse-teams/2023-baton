package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.RunnerPostController;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.CompanyFixture.company;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.GithubUrlFixture.githubUrl;
import static touch.baton.fixture.vo.ImageUrlFixture.imageUrl;
import static touch.baton.fixture.vo.MemberNameFixture.memberName;
import static touch.baton.fixture.vo.OauthIdFixture.oauthId;
import static touch.baton.fixture.vo.SocialIdFixture.socialId;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

@WebMvcTest(RunnerPostController.class)
public class RunnerPostReadWithLoginedSupporterApiTest extends RestdocsConfig {

    @MockBean
    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        final RunnerPostController runnerPostController = new RunnerPostController(runnerPostService);
        restdocsSetUp(runnerPostController);
    }

    @DisplayName("로그인한 서포터가 참여한 러너 게시글 페이징 조회 API")
    @Test
    void readRunnerPostsByLoginedSupporterAndReviewStatus() throws Exception {
        // given
        final Runner runnerJudy = RunnerFixture.createRunner(MemberFixture.createJudy());
        final String socialId = "ditooSocialId";
        final Member loginedMember = MemberFixture.create(
                memberName("디투"),
                socialId(socialId),
                oauthId("abcd"),
                githubUrl("naver.com"),
                company("우아한테크코스"),
                imageUrl("profile.jpg")
        );
        final Supporter loginedSupporter = SupporterFixture.create(loginedMember);
        final String token = getAccessTokenBySocialId(socialId);

        final Tag javaTag = TagFixture.create(tagName("자바"));
        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
        final RunnerPost runnerPost = RunnerPostFixture.create(runnerJudy, deadline, List.of(javaTag));
        runnerPost.assignSupporter(loginedSupporter);

        // when
        final RunnerPost spyRunnerPost = spy(runnerPost);
        final Supporter spyLoginedSupporter = spy(loginedSupporter);
        when(oauthSupporterRepository.joinByMemberSocialId(any())).thenReturn(Optional.ofNullable(spyLoginedSupporter));
        when(spyRunnerPost.getId()).thenReturn(1L);

        final List<RunnerPost> runnerPosts = List.of(spyRunnerPost);
        final PageRequest pageOne = PageRequest.of(1, 10);
        final PageImpl<RunnerPost> pageRunnerPosts = new PageImpl<>(runnerPosts, pageOne, runnerPosts.size());
        when(runnerPostService.readRunnerPostsBySupporterIdAndReviewStatus(any(), any(), any()))
                .thenReturn(pageRunnerPosts);
        when(runnerPostService.readCountsByRunnerPostIds(anyList())).thenReturn(List.of(1L));

        // then
        mockMvc.perform(get("/api/v1/posts/runner/me/supporter")
                        .header(AUTHORIZATION, "Bearer " + token)
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
                                fieldWithPath("pageInfo.isFirst").type(BOOLEAN).description("첫 번째 페이지인지"),
                                fieldWithPath("pageInfo.isLast").type(BOOLEAN).description("마지막 페이지 인지"),
                                fieldWithPath("pageInfo.hasNext").type(BOOLEAN).description("다음 페이지가 있는지"),
                                fieldWithPath("pageInfo.totalPages").type(NUMBER).description("총 페이지 수"),
                                fieldWithPath("pageInfo.totalElements").type(NUMBER).description("총 데이터 수"),
                                fieldWithPath("pageInfo.currentPage").type(NUMBER).description("현재 페이지 번호"),
                                fieldWithPath("pageInfo.currentSize").type(NUMBER).description("현재 페이지 데이터 수"),
                                fieldWithPath("data.[].runnerPostId").type(NUMBER).description("러너 게시글 식별자값(id)"),
                                fieldWithPath("data.[].title").type(STRING).description("러너 게시글 제목"),
                                fieldWithPath("data.[].deadline").type(STRING).description("러너 게시글의 마감 기한"),
                                fieldWithPath("data.[].tags").type(ARRAY).description("러너 게시글 태그 목록"),
                                fieldWithPath("data.[].watchedCount").type(NUMBER).description("러너 게시글의 조회수"),
                                fieldWithPath("data.[].applicantCount").type(NUMBER).description("러너 게시글에 신청한 서포터 수"),
                                fieldWithPath("data.[].reviewStatus").type(STRING).description("러너 게시글 리뷰 상태")
                        ))
                );
    }
}
