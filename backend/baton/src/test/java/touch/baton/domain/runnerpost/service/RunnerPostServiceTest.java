package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
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
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.tag.RunnerPostTags;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaConfig.class)
@DataJpaTest
class RunnerPostServiceTest {

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
            .build();

    private final Member supporterMember = Member.builder()
            .memberName(new MemberName("서포터 사용자"))
            .email(new Email("test@test.co.kr"))
            .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
            .githubUrl(new GithubUrl("github.com/pobi"))
            .company(new Company("우아한형제들"))
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
        memberRepository.save(runnerMember);
        memberRepository.save(supporterMember);
        runnerRepository.save(runner);
        supporterRepository.save(supporter);
    }

    @DisplayName("runner 가 작성한 모든 RunnerPost 를 조회한다.")
    @Test
    void findByRunnerId() {
        // given
        RunnerPostService runnerPostService = new RunnerPostService(runnerPostRepository);
        RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("title"))
                .contents(new Contents("contents"))
                .pullRequestUrl(new PullRequestUrl("url"))
                .deadline(new Deadline(LocalDateTime.now()))
                .watchedCount(new WatchedCount(2))
                .chattingRoomCount(new ChattingRoomCount(3))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();

        Long expected = runnerPost.getRunner().getId();
        runnerPostRepository.save(runnerPost);

        // when
        List<RunnerPost> runnerPosts = runnerPostService.findByRunnerId(expected);
        Long actual = runnerPosts.get(0).getRunner().getId();

        // then
        Assertions.assertEquals(actual, expected);
    }

    @DisplayName("supporter 가 리뷰한 모든 RunnerPost 를 조회한다.")
    @Test
    void findBySupporterId() {
        RunnerPostService runnerPostService = new RunnerPostService(runnerPostRepository);
        RunnerPost expected = RunnerPost.builder()
                .title(new Title("title"))
                .contents(new Contents("contents"))
                .pullRequestUrl(new PullRequestUrl("url"))
                .deadline(new Deadline(LocalDateTime.now()))
                .watchedCount(new WatchedCount(2))
                .chattingRoomCount(new ChattingRoomCount(3))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();

        Long runnerId = expected.getSupporter().getId();
        runnerPostRepository.save(expected);

        // when
        RunnerPost actual = runnerPostService.findBySupporterId(runnerId).get(0);

        // then
        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("title 을 제목으로 가진 RunnerPost 를 조회한다.")
    @Test
    void testFindByTitle() {
        // given
        String title = "title";
        RunnerPostService runnerPostService = new RunnerPostService(runnerPostRepository);
        RunnerPost expected = RunnerPost.builder()
                .title(new Title(title))
                .contents(new Contents("contents"))
                .pullRequestUrl(new PullRequestUrl("url"))
                .deadline(new Deadline(LocalDateTime.now()))
                .watchedCount(new WatchedCount(2))
                .chattingRoomCount(new ChattingRoomCount(3))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();

        runnerPostRepository.save(expected);

        // when
        RunnerPost actual = runnerPostService.findByTitle(title);

        // then
        assertThat(expected).isEqualTo(actual);
    }
}
