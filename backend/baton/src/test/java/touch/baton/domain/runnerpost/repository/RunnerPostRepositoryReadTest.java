package touch.baton.domain.runnerpost.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.CuriousContents;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ImplementedContents;
import touch.baton.domain.runnerpost.vo.PostscriptContents;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.fixture.domain.RunnerTechnicalTagsFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RunnerPostRepositoryReadTest extends RepositoryTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @DisplayName("RunnerPost 식별자값으로 RunnerPost 을 삭제한다.")
    @Test
    void success_deleteByRunnerPostId() {
        // given
        final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("홍혁준"))
                .build();
        final Member saveMember = memberRepository.saveAndFlush(member);

        final Runner runner = Runner.builder()
                .member(saveMember)
                .runnerTechnicalTags(RunnerTechnicalTagsFixture.create(new ArrayList<>()))
                .build();

        final Runner saveRunner = runnerRepository.saveAndFlush(runner);

        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("제 코드 리뷰 좀 해주세요!!"))
                .implementedContents(new ImplementedContents("제 코드는 클린코드가 맞을까요?"))
                .curiousContents(new CuriousContents("저는 클린코드가 궁금해요."))
                .postscriptContents(new PostscriptContents("저 상처 잘 받으니깐 부드럽게 말해주세요."))
                .deadline(new Deadline(LocalDateTime.now()))
                .pullRequestUrl(new PullRequestUrl("https://"))
                .watchedCount(new WatchedCount(1))
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .runner(saveRunner)
                .supporter(null)
                .build();
        final Long saveRunnerPostId = runnerPostRepository.saveAndFlush(runnerPost).getId();

        // when
        runnerPostRepository.deleteById(saveRunnerPostId);

        final Optional<RunnerPost> maybeRunnerPost = runnerPostRepository.findById(saveRunnerPostId);

        // then
        assertThat(maybeRunnerPost).isNotPresent();
    }
}
