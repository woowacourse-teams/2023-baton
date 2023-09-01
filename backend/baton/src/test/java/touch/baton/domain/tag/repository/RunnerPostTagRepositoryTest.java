package touch.baton.domain.tag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.TagName;
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
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagReducedName;
import touch.baton.fixture.domain.RunnerTechnicalTagsFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RunnerPostTagRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @Autowired
    private RunnerPostTagRepository runnerPostTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @DisplayName("RunnerPostTag 의 식별자값 목록으로 Tag 목록을 조회한다.")
    @Test
    void success_joinTagByRunnerPostIds() {
        // given
        final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("김석호"))
                .build();
        final Member saveMember = memberRepository.saveAndFlush(member);

        final Runner runner = Runner.builder()
                .member(saveMember)
                .runnerTechnicalTags(RunnerTechnicalTagsFixture.create(new ArrayList<>()))
                .build();
        final Runner saveRunner = runnerRepository.saveAndFlush(runner);

        final LocalDateTime deadline = LocalDateTime.now();
        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("제 코드 리뷰 좀 해주세요!!"))
                .contents(new Contents("제 코드는 클린코드가 맞을까요?"))
                .deadline(new Deadline(deadline))
                .pullRequestUrl(new PullRequestUrl("https://"))
                .watchedCount(new WatchedCount(1))
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .runner(saveRunner)
                .supporter(null)
                .build();
        runnerPostRepository.saveAndFlush(runnerPost);

        final Tag tag = Tag.builder()
                .tagName(new TagName("자바"))
                .tagReducedName(TagReducedName.from("자바"))
                .build();
        tagRepository.save(tag);

        final RunnerPostTag runnerPostTag = RunnerPostTag.builder()
                .runnerPost(runnerPost)
                .tag(tag)
                .build();
        runnerPostTagRepository.save(runnerPostTag);

        // when
        final List<RunnerPostTag> joinRunnerPostTags
                = runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());

        // then
        assertThat(joinRunnerPostTags).containsExactly(runnerPostTag);
    }

    @DisplayName("RunnerPostTag 의 식별자값 목록이 비어있을 때 빈 컬렉션을 반환한다.")
    @Test
    void success_joinTagByRunnerPostIds_if_tag_is_empty() {
        // given
        final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("김석호"))
                .build();
        final Member saveMember = memberRepository.saveAndFlush(member);

        final Runner runner = Runner.builder()
                .member(saveMember)
                .runnerTechnicalTags(RunnerTechnicalTagsFixture.create(new ArrayList<>()))
                .build();

        final Runner saveRunner = runnerRepository.saveAndFlush(runner);

        final LocalDateTime deadline = LocalDateTime.now();
        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("제 코드 리뷰 좀 해주세요!!"))
                .contents(new Contents("제 코드는 클린코드가 맞을까요?"))
                .deadline(new Deadline(deadline))
                .pullRequestUrl(new PullRequestUrl("https://"))
                .watchedCount(new WatchedCount(1))
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .runner(saveRunner)
                .supporter(null)
                .build();
        runnerPostRepository.saveAndFlush(runnerPost);

        // when
        final List<RunnerPostTag> joinRunnerPostTags
                = runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());

        // then
        assertThat(joinRunnerPostTags).isEmpty();
    }
}
