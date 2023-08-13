package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerTechnicalTagsFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostServiceReadTest extends ServiceTestConfig {

    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        runnerPostService = new RunnerPostService(
                runnerPostRepository,
                runnerPostTagRepository,
                tagRepository,
                supporterRepository,
                supporterRunnerPostRepository
        );
    }

    @DisplayName("RunnerPost 식별자로 RunnerPost 를 조회한다.")
    @Test
    void success_findByRunnerPostId() {
        // given
        final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("홍혁준"))
                .build();
        memberRepository.save(member);

        final Runner runner = Runner.builder()
                .member(member)
                .runnerTechnicalTags(RunnerTechnicalTagsFixture.create(new ArrayList<>()))
                .build();
        runnerRepository.save(runner);

        final LocalDateTime deadline = now();
        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("제 코드 리뷰 좀 해주세요!!"))
                .contents(new Contents("제 코드는 클린코드가 맞을까요?"))
                .deadline(new Deadline(deadline))
                .pullRequestUrl(new PullRequestUrl("https://"))
                .watchedCount(new WatchedCount(0))
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .reviewStatus(NOT_STARTED)
                .runner(runner)
                .supporter(null)
                .build();
        runnerPostRepository.save(runnerPost);

        final Tag tag = Tag.builder()
                .tagName(new TagName("자바"))
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

    @DisplayName("RunnerPost 식별자로 존재하지 않는 RunnerPost 를 조회할 경우 예외가 발생한다.")
    @Test
    void fail_findByRunnerPostId_if_runner_post_is_null() {
        assertThatThrownBy(() -> runnerPostService.readByRunnerPostId(0L))
                .isInstanceOf(RunnerPostBusinessException.class)
                .hasMessage("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다.");
    }

    @DisplayName("Runner 식별자값으로 RunnerPost 를 조회한다.")
    @Test
    void success_findByRunnerId() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        memberRepository.save(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        runnerRepository.save(runner);
        final RunnerPost expected = RunnerPostFixture.create(runner, new Deadline(now().plusHours(100)));
        runnerPostRepository.save(expected);

        // when
        final List<RunnerPost> actual = runnerPostService.readRunnerPostsByRunnerId(runner.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            softly.assertThat(actual.get(0)).isEqualTo(expected);
        });
    }

    @DisplayName("Supporter 와 ReviewStatus 로 RunnerPost 를 최신순으로 조회한다.")
    @Test
    void readRunnerPostBySupporterAndReviewStatus() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        memberRepository.save(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        runnerRepository.save(runner);

        final Member loginedMember = MemberFixture.createJudy();
        memberRepository.save(loginedMember);
        final Supporter loginedSupporter = SupporterFixture.create(loginedMember);
        supporterRepository.save(loginedSupporter);

        final ReviewStatus reviewStatus = NOT_STARTED;

        final RunnerPost runnerPost1 = RunnerPostFixture.create(runner, loginedSupporter, new Deadline(LocalDateTime.now().plusHours(100)), reviewStatus);
        final RunnerPost firstSavedRunnerPost = runnerPostRepository.save(runnerPost1);
        final RunnerPost runnerPost2 = RunnerPostFixture.create(runner, loginedSupporter, new Deadline(LocalDateTime.now().plusHours(10)), reviewStatus);
        final RunnerPost secondSavedRunnerPost = runnerPostRepository.save(runnerPost2);
        final RunnerPost runnerPost3 = RunnerPostFixture.create(runner, loginedSupporter, new Deadline(LocalDateTime.now().plusHours(20)), reviewStatus);
        final RunnerPost thirdSavedRunnerPost = runnerPostRepository.save(runnerPost3);
        final List<RunnerPost> expected = List.of(thirdSavedRunnerPost, secondSavedRunnerPost, firstSavedRunnerPost);

        // when
        final List<RunnerPost> actual = runnerPostService.readRunnerPostBySupporterAndReviewStatus(loginedSupporter, reviewStatus);

        // then
        assertSoftly(softly -> {
            softly.assertThat(expected).hasSameSizeAs(actual);
            softly.assertThat(expected.get(0)).isEqualTo(actual.get(0));
            softly.assertThat(expected.get(1)).isEqualTo(actual.get(1));
            softly.assertThat(expected.get(2)).isEqualTo(actual.get(2));
        });
    }

    @DisplayName("Supporter 외래키와 ReviewStatus 로 러너 게시글을 조회한다.")
    @Test
    void readRunnerPostsBySupporterIdAndReviewStatus() {
        // given
        final Member savedMemberEthan = memberRepository.save(MemberFixture.createEthan());
        final Runner savedRunnerEthan = runnerRepository.save(RunnerFixture.createRunner(savedMemberEthan));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPost = RunnerPostFixture.create(savedRunnerEthan, deadline(now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostRepository.save(runnerPost);
        savedRunnerPost.assignSupporter(savedSupporterHyena);

        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(runnerPost, savedSupporterHyena));

        // when
        final PageRequest pageable = PageRequest.of(0, 10);
        final Page<RunnerPost> pageRunnerPosts
                = runnerPostService.readRunnerPostsBySupporterIdAndReviewStatus(pageable, savedSupporterHyena.getId(), ReviewStatus.IN_PROGRESS);

        // then
        assertAll(
                () -> assertThat(pageRunnerPosts.getPageable()).isEqualTo(pageable),
                () -> assertThat(pageRunnerPosts.getContent()).containsExactly(savedRunnerPost)
        );
    }
}
