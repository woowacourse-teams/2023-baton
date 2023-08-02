package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.common.vo.ChattingCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagCount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RunnerPostServiceReadTest extends ServiceTestConfig {

    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        runnerPostService = new RunnerPostService(runnerPostRepository, runnerPostTagRepository, tagRepository, supporterRepository);
    }

    @DisplayName("RunnerPost 식별자값으로 RunnerPost 를 조회한다.")
    @Test
    void success_findByRunnerPostId() {
        // given
        final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .email(new Email("test@test.co.kr"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("홍혁준"))
                .build();
        memberRepository.save(member);

        final Runner runner = Runner.builder()
                .totalRating(new TotalRating(100))
                .grade(Grade.BARE_FOOT)
                .member(member)
                .build();
        runnerRepository.save(runner);

        final LocalDateTime deadline = LocalDateTime.now();
        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("제 코드 리뷰 좀 해주세요!!"))
                .contents(new Contents("제 코드는 클린코드가 맞을까요?"))
                .deadline(new Deadline(deadline))
                .pullRequestUrl(new PullRequestUrl("https://"))
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .runner(runner)
                .supporter(null)
                .build();
        runnerPostRepository.save(runnerPost);

        final Tag tag = Tag.builder()
                .tagName(new TagName("자바"))
                .tagCount(new TagCount(1))
                .build();
        tagRepository.save(tag);

        final RunnerPostTag runnerPostTag = RunnerPostTag.builder()
                .runnerPost(runnerPost)
                .tag(tag)
                .build();
        runnerPost.addAllRunnerPostTags(List.of(runnerPostTag));

        // when
        final RunnerPost findRunnerPost = runnerPostService.readByRunnerPostId(runnerPost.getId());

        // then
        assertThat(findRunnerPost)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(runnerPost);
    }

    @DisplayName("RunnerPost 식별자값으로 존재하지 않는 RunnerPost 을 조회할 경우 예외가 발생한다.")
    @Test
    void fail_findByRunnerPostId_if_runner_post_is_null() {
        assertThatThrownBy(() -> runnerPostService.readByRunnerPostId(0L))
                .isInstanceOf(RunnerPostBusinessException.class)
                .hasMessage("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다.");
    }
}
