package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.ServiceTestConfig;
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
import touch.baton.domain.runnerpost.vo.CuriousContents;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ImplementedContents;
import touch.baton.domain.runnerpost.vo.IsReviewed;
import touch.baton.domain.runnerpost.vo.PostscriptContents;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagReducedName;
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
import static touch.baton.domain.runnerpost.vo.ReviewStatus.IN_PROGRESS;
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
                .implementedContents(new ImplementedContents("제 코드는 클린코드가 맞을까요?"))
                .curiousContents(new CuriousContents("궁금해요."))
                .postscriptContents(new PostscriptContents("잘 부탁드립니다."))
                .deadline(new Deadline(deadline))
                .pullRequestUrl(new PullRequestUrl("https://"))
                .watchedCount(new WatchedCount(0))
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .reviewStatus(NOT_STARTED)
                .isReviewed(IsReviewed.notReviewed())
                .runner(runner)
                .supporter(null)
                .build();
        runnerPostRepository.save(runnerPost);

        final Tag tag = Tag.builder()
                .tagName(new TagName("자바"))
                .tagReducedName(TagReducedName.from("자바"))
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

    @DisplayName("ReviewStatus 로 RunnerPost 를 전체 조회한다.")
    @Test
    void success_readRunnerPostsByReviewStatus() {
        // given
        final Member memberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner runnerDitoo = runnerRepository.save(RunnerFixture.createRunner(memberDitoo));
        final Member memberJudy = memberRepository.save(MemberFixture.createJudy());
        final Supporter supporterJudy = supporterRepository.save(SupporterFixture.create(memberJudy));

        final RunnerPost inProgressRunnerPost = RunnerPostFixture.create(runnerDitoo, deadline(now().plusHours(100)));
        inProgressRunnerPost.assignSupporter(supporterJudy);
        final RunnerPost savedInProgressRunnerPost = runnerPostRepository.save(inProgressRunnerPost);

        // when
        final PageRequest pageable = PageRequest.of(0, 10);
        final Page<RunnerPost> actualInProgressRunnerPosts = runnerPostService.readRunnerPostsByReviewStatus(pageable, IN_PROGRESS);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actualInProgressRunnerPosts.getPageable()).isEqualTo(pageable);
            softly.assertThat(actualInProgressRunnerPosts.getContent()).containsExactly(savedInProgressRunnerPost);
        });
    }

    @DisplayName("Supporter 외래키와 ReviewStatus 가 NOT_STARTED 가 아닌 것으로 러너 게시글을 조회한다.")
    @Test
    void readRunnerPostsBySupporterIdAndReviewStatusIsNot_NOT_STARTED() {
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
                = runnerPostService.readRunnerPostsBySupporterIdAndReviewStatus(pageable, savedSupporterHyena.getId(), IN_PROGRESS);

        // then
        assertAll(
                () -> assertThat(pageRunnerPosts.getPageable()).isEqualTo(pageable),
                () -> assertThat(pageRunnerPosts.getContent()).containsExactly(savedRunnerPost)
        );
    }

    @DisplayName("Runner 외래키와 ReviewStatus 로 러너 게시글을 조회한다.")
    @Test
    void readRunnerPostsByRunnerIdAndReviewStatus() {
        // given
        final Member savedMemberEthan = memberRepository.save(MemberFixture.createEthan());
        final Runner savedRunnerEthan = runnerRepository.save(RunnerFixture.createRunner(savedMemberEthan));

        final RunnerPost runnerPost = RunnerPostFixture.create(savedRunnerEthan, deadline(now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostRepository.save(runnerPost);

        // when
        final PageRequest pageable = PageRequest.of(0, 10);
        final Page<RunnerPost> pageRunnerPosts
                = runnerPostService.readRunnerPostsByRunnerIdAndReviewStatus(pageable, savedRunnerEthan.getId(), ReviewStatus.NOT_STARTED);

        // then
        assertAll(
                () -> assertThat(pageRunnerPosts.getPageable()).isEqualTo(pageable),
                () -> assertThat(pageRunnerPosts.getContent()).containsExactly(savedRunnerPost)
        );
    }

    @DisplayName("RunnerPost 식별자값으로 Supporter 지원자수를 count 한다.")
    @Test
    void readCountByRunnerPostId() {
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
        final long foundApplicantCount = runnerPostService.readCountByRunnerPostId(savedRunnerPost.getId());

        // then
        assertThat(foundApplicantCount).isEqualTo(1);
    }

    @DisplayName("RunnerPost 식별자값으로 찾은 Supporter 지원자가 아무도 없을 경우 count 로 0을 반환한다.")
    @Test
    void readCountByRunnerPostId_is_null_then_return_zero() {
        // given
        final Member savedMemberEthan = memberRepository.save(MemberFixture.createEthan());
        final Runner savedRunnerEthan = runnerRepository.save(RunnerFixture.createRunner(savedMemberEthan));

        final RunnerPost runnerPost = RunnerPostFixture.create(savedRunnerEthan, deadline(now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostRepository.save(runnerPost);

        // when
        final long foundApplicantCount = runnerPostService.readCountByRunnerPostId(savedRunnerPost.getId());

        // then1
        assertThat(foundApplicantCount).isZero();
    }

    @DisplayName("Supporter 외래키와 ReviewStatus 가 NOT_STARTED 로 러너 게시글을 조회한다.")
    @Test
    void readRunnerPostsBySupporterIdAndReviewStatusIs_NOT_STARTED() {
        // given
        final Member savedMemberEthan = memberRepository.save(MemberFixture.createEthan());
        final Runner savedRunnerEthan = runnerRepository.save(RunnerFixture.createRunner(savedMemberEthan));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPost = RunnerPostFixture.create(savedRunnerEthan, deadline(now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostRepository.save(runnerPost);

        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(runnerPost, savedSupporterHyena));

        // when
        final PageRequest pageable = PageRequest.of(0, 10);
        final Page<RunnerPost> pageRunnerPosts
                = runnerPostService.readRunnerPostsBySupporterIdAndReviewStatus(pageable, savedSupporterHyena.getId(), NOT_STARTED);

        // then
        assertAll(
                () -> assertThat(pageRunnerPosts.getPageable()).isEqualTo(pageable),
                () -> assertThat(pageRunnerPosts.getContent()).containsExactly(savedRunnerPost)
        );
    }

    @DisplayName("Member 가 RunnerPost 에 지원한 이력이 있을 경우 true 를 반환한다.")
    @Test
    void existsRunnerPostApplicantByRunnerPostIdAndMemberId() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPost, savedSupporterHyena));

        // when
        final boolean isApplicantHistoryExist = runnerPostService.existsRunnerPostApplicantByRunnerPostIdAndMemberId(
                savedRunnerPost.getId(),
                savedMemberHyena.getId()
        );

        // then
        assertThat(isApplicantHistoryExist).isTrue();
    }

    @DisplayName("Member 가 RunnerPost 에 지원한 이력을 조회할 때 RunnerPost 자체가 없으면 false 를 반환한다.")
    @Test
    void existsRunnerPostApplicantByRunnerPostIdAndMemberId_if_runnerPost_is_not_exist_then_return_false() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createHyena());

        // when
        final Long notExistRunnerPostId = -1L;
        final boolean isApplicantHistoryExist = runnerPostService.existsRunnerPostApplicantByRunnerPostIdAndMemberId(
                notExistRunnerPostId,
                savedMemberHyena.getId()
        );

        // then
        assertThat(isApplicantHistoryExist).isFalse();
    }

    @DisplayName("Member 가 RunnerPost 에 지원한 이력이 없을 경우 false 를 반환한다.")
    @Test
    void existsRunnerPostApplicantByRunnerPostIdAndMemberId_if_member_is_not_exist_then_return_false() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final RunnerPost savedRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

        // when
        final Long notExistMemberId = -1L;
        final boolean isApplicantHistoryExist = runnerPostService.existsRunnerPostApplicantByRunnerPostIdAndMemberId(
                savedRunnerPost.getId(),
                notExistMemberId
        );

        // then
        assertThat(isApplicantHistoryExist).isFalse();
    }
}
