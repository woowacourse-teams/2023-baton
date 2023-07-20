package touch.baton.domain.runnerpost.controller.read;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import touch.baton.config.JpaConfig;
import touch.baton.domain.common.vo.ChattingRoomCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.RunnerPostController;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.tag.RunnerPostTags;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JpaConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class RunnerPostControllerReadTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private RunnerPostService runnerPostService;

    @Autowired
    private RunnerPostController runnerPostController;

    @Autowired
    private RunnerPostRepository runnerPostRepository;
    @Autowired
    private RunnerRepository runnerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SupporterRepository supporterRepository;

    private final Member runnerMember = Member.builder()
            .memberName(new MemberName("러너 사용자"))
            .email(new Email("test@test.co.kr"))
            .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
            .githubUrl(new GithubUrl("github.com/hyena0608"))
            .company(new Company("우아한테크코스"))
            .imageUrl(new ImageUrl("imageUrl"))
            .build();

    private final Member supporterMember = Member.builder()
            .memberName(new MemberName("서포터 사용자"))
            .email(new Email("test@test.co.kr"))
            .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
            .githubUrl(new GithubUrl("github.com/pobi"))
            .company(new Company("우아한형제들"))
            .imageUrl(new ImageUrl("imageUrl"))
            .build();

    private final Runner runner = Runner.builder()
            .totalRating(new TotalRating(2))
            .grade(Grade.BARE_FOOT)
            .member(runnerMember)
            .build();

    private final Supporter supporter = Supporter.builder()
            .reviewCount(new ReviewCount(10))
            .starCount(new StarCount(10))
            .totalRating(new TotalRating(100))
            .grade(Grade.BARE_FOOT)
            .member(supporterMember)
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(runnerPostController).build();

        memberRepository.save(runnerMember);
        memberRepository.save(supporterMember);
        runnerRepository.save(runner);
        supporterRepository.save(supporter);
    }

    @DisplayName("response 가 json 형식인지 테스트 한다.")
    @Test
    void read() throws Exception {
        // given, when
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/posts/runner/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertNotNull(content);
    }
}
