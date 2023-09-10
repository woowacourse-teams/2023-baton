package touch.baton.domain.runnerpost;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import touch.baton.domain.runnerpost.exception.RunnerPostDomainException;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ImplementedContents;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;
import touch.baton.fixture.domain.RunnerTechnicalTagsFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerPostTest {

    private final Member runnerMember = Member.builder()
            .memberName(new MemberName("러너 사용자"))
            .socialId(new SocialId("testSocialId"))
            .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
            .githubUrl(new GithubUrl("github.com/hyena0608"))
            .company(new Company("우아한테크코스"))
            .imageUrl(new ImageUrl("김석호"))
            .build();

    private final Member supporterMember = Member.builder()
            .memberName(new MemberName("서포터 사용자"))
            .socialId(new SocialId("testSocialId"))
            .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
            .githubUrl(new GithubUrl("github.com/pobi"))
            .company(new Company("우아한형제들"))
            .imageUrl(new ImageUrl("김석호"))
            .build();

    private final Runner runner = Runner.builder()
            .member(runnerMember)
            .runnerTechnicalTags(RunnerTechnicalTagsFixture.create(new ArrayList<>()))
            .build();

    private final Supporter supporter = Supporter.builder()
            .reviewCount(new ReviewCount(10))
            .member(supporterMember)
            .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
            .build();

    @DisplayName("runnerPostTags 전체를 추가할 수 있다.")
    @Test
    void addAllRunnerPostTags() {
        // given
        final String title = "JPA 리뷰 부탁 드려요.";
        final String contents = "넘나 어려워요.";
        final String pullRequestUrl = "https://github.com/cookienc";
        final LocalDateTime deadline = LocalDateTime.of(2099, 12, 12, 0, 0);
        final RunnerPost runnerPost = RunnerPost.newInstance(title, contents, pullRequestUrl, deadline, runner);
        final RunnerPostTag java = RunnerPostTag.builder()
                .tag(Tag.newInstance("Java"))
                .runnerPost(runnerPost)
                .build();
        final RunnerPostTag spring = RunnerPostTag.builder()
                .tag(Tag.newInstance("Spring"))
                .runnerPost(runnerPost)
                .build();

        final List<String> expectedTagNames = Arrays.asList("Java", "Spring");

        // when
        runnerPost.addAllRunnerPostTags(List.of(java, spring));
         List<RunnerPostTag> runnerPostTags = runnerPost.getRunnerPostTags().getRunnerPostTags();
        final List<String> actualTagNames = runnerPostTags.stream()
                .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                .collect(Collectors.toList());

        // then
        assertSoftly(softAssertions -> {
            assertThat(runnerPost.getRunnerPostTags().getRunnerPostTags()).hasSize(2);
            assertThat(actualTagNames).containsExactlyElementsOf(expectedTagNames);
        });
    }

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> RunnerPost.builder()
                    .title(new Title("JPA 정복"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("supporterProfile 에 null 이 들어간 경우 아직 리뷰어 할당이 되지 않은 것이다.")
        @Test
        void success_if_supporter_is_null() {
            assertThatCode(() -> RunnerPost.builder()
                    .title(new Title("아이"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(null)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("title 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_title_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(null)
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 title 은 null 일 수 없습니다.");
        }

        @DisplayName("contents 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_contents_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(new Title("헤나"))
                    .contents(null)
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 contents 는 null 일 수 없습니다.");
        }

        @DisplayName("pull request url 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_pullRequestUrl_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(new Title("하이하이"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(null)
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 pullRequestUrl 은 null 일 수 없습니다.");
        }

        @DisplayName("deadline 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_deadline_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(new Title("아이"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(null)
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 deadline 은 null 일 수 없습니다.");
        }

        @DisplayName("watched count 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_watchedCount_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(new Title("아이"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(null)
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 watchedCount 는 null 일 수 없습니다.");
        }

        @DisplayName("runner 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_runner_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(new Title("아이"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(null)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 runner 는 null 일 수 없습니다.");
        }

        @DisplayName("runnerPostTags 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_runnerPostTags_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(new Title("아이"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(null)
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 runnerPostTags 는 null 일 수 없습니다.");
        }

        @DisplayName("태그, 조회수, 채팅수가 초기화된 RunnerPost 를 생성할 수 있다.")
        @Test
        void createDefaultRunnerPost() {
            // given
            final String title = "JPA 리뷰 부탁 드려요.";
            final String contents = "넘나 어려워요.";
            final String pullRequestUrl = "https://github.com/cookienc";
            final LocalDateTime deadline = LocalDateTime.of(2099, 12, 12, 0, 0);
            final RunnerPost runnerPost = RunnerPost.newInstance(title, contents, pullRequestUrl, deadline, runner);

            // when, then
            assertAll(
                    () -> assertThat(runnerPost.getTitle()).isEqualTo(new Title(title)),
                    () -> assertThat(runnerPost.getImplementedContents()).isEqualTo(new ImplementedContents(contents)),
                    () -> assertThat(runnerPost.getPullRequestUrl()).isEqualTo(new PullRequestUrl(pullRequestUrl)),
                    () -> assertThat(runnerPost.getDeadline()).isEqualTo(new Deadline(deadline)),
                    () -> assertThat(runnerPost.getRunnerPostTags()).isNotNull(),
                    () -> assertThat(runnerPost.getWatchedCount()).isEqualTo(new WatchedCount(0))
            );
        }
    }

    @DisplayName("Supporter 할당")
    @Nested
    class AssignSupporter {

        @DisplayName("RunnerPost 내부의 Supporter 가 null 이며 ReviewStatus 가 NOT_STARTED 이어야 하며 Deadline 이 끝나지 않은 경우 성공한다.")
        @Test
        void success_supporter_is_null_and_deadline_is_not_end() {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("JPA 정복"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now().plusHours(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(null)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // then
            assertThatCode(() -> runnerPost.assignSupporter(supporter))
                    .doesNotThrowAnyException();
        }

        @DisplayName("RunnerPost 내부의 Supporter 가 null 이 아닐 때 예외가 발생한다.")
        @Test
        void fail_supporter_is_not_null() {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("JPA 정복"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now().plusHours(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // then
            assertThatThrownBy(() -> runnerPost.assignSupporter(supporter))
                    .isInstanceOf(RunnerPostDomainException.class);
        }

        @DisplayName("RunnerPost 의 마감 기한이 이미 끝났을 때 예외가 발생한다.")
        @Test
        void fail_deadline_is_already_end() {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("JPA 정복"))
                    .contents(new ImplementedContents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now().minusDays(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // then
            assertThatThrownBy(() -> runnerPost.assignSupporter(supporter))
                    .isInstanceOf(RunnerPostDomainException.class);
        }
    }

    @DisplayName("RunnerPost ReviewStatus 수정")
    @Nested
    class UpdateReviewStatus {

        private static Stream<Arguments> reviewStatusDummy() {
            return Arrays.stream(ReviewStatus.values())
                    .map(Arguments::arguments);
        }

        @DisplayName("IN_PROGRESS 에서 DONE 으로 수정 성공한다.")
        @Test
        void success_IN_PROGRESS__to_DONE() {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("러너가 작성하는 리뷰 요청 게시글의 테스트 제목입니다."))
                    .contents(new ImplementedContents("안녕하세요. 테스트 내용입니다."))
                    .pullRequestUrl(new PullRequestUrl("https://github.com"))
                    .deadline(new Deadline(LocalDateTime.now().plusHours(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.IN_PROGRESS)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // when
            runnerPost.updateReviewStatus(ReviewStatus.DONE);

            // then
            assertThat(runnerPost.getReviewStatus()).isEqualTo(ReviewStatus.DONE);
        }

        @DisplayName("NOT_STARTED 에서 IN_PROGRESS 으로 수정 실패한다.")
        @Test
        void fail_NOT_STARTED__to_IN_PROGRESS() {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("러너가 작성하는 리뷰 요청 게시글의 테스트 제목입니다."))
                    .contents(new ImplementedContents("안녕하세요. 테스트 내용입니다."))
                    .pullRequestUrl(new PullRequestUrl("https://github.com"))
                    .deadline(new Deadline(LocalDateTime.now().plusHours(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // when & then
            assertThatThrownBy(() -> runnerPost.updateReviewStatus(ReviewStatus.IN_PROGRESS))
                    .isInstanceOf(RunnerPostDomainException.class);
        }

        @DisplayName("NOT_STARTED 에서 DONE 으로 수정 실패한다.")
        @Test
        void fail_NOT_STARTED__to_DONE() {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("러너가 작성하는 리뷰 요청 게시글의 테스트 제목입니다."))
                    .contents(new ImplementedContents("안녕하세요. 테스트 내용입니다."))
                    .pullRequestUrl(new PullRequestUrl("https://github.com"))
                    .deadline(new Deadline(LocalDateTime.now().plusHours(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.DONE)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // when & then
            assertThatThrownBy(() -> runnerPost.updateReviewStatus(ReviewStatus.DONE))
                    .isInstanceOf(RunnerPostDomainException.class);
        }

        @DisplayName("DONE 에서 NOT_STARTED 으로 수정 실패한다.")
        @Test
        void fail_DONE_to_NOT_STARTED() {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("러너가 작성하는 리뷰 요청 게시글의 테스트 제목입니다."))
                    .contents(new ImplementedContents("안녕하세요. 테스트 내용입니다."))
                    .pullRequestUrl(new PullRequestUrl("https://github.com"))
                    .deadline(new Deadline(LocalDateTime.now().plusHours(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.DONE)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // when & then
            assertThatThrownBy(() -> runnerPost.updateReviewStatus(ReviewStatus.NOT_STARTED))
                    .isInstanceOf(RunnerPostDomainException.class);
        }

        @DisplayName("DONE 에서 IN_PROGRESS 으로 수정 실패한다.")
        @Test
        void fail_DONE_to_IN_PROGRESS() {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("러너가 작성하는 리뷰 요청 게시글의 테스트 제목입니다."))
                    .contents(new ImplementedContents("안녕하세요. 테스트 내용입니다."))
                    .pullRequestUrl(new PullRequestUrl("https://github.com"))
                    .deadline(new Deadline(LocalDateTime.now().plusHours(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(ReviewStatus.DONE)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // when & then
            assertThatThrownBy(() -> runnerPost.updateReviewStatus(ReviewStatus.IN_PROGRESS))
                    .isInstanceOf(RunnerPostDomainException.class);
        }

        @DisplayName("같은 ReviewStatus 로 수정할 경우 실패한다.")
        @ParameterizedTest
        @MethodSource("reviewStatusDummy")
        void fail_same_to_same(final ReviewStatus reviewStatus) {
            // given
            final RunnerPost runnerPost = RunnerPost.builder()
                    .title(new Title("러너가 작성하는 리뷰 요청 게시글의 테스트 제목입니다."))
                    .contents(new ImplementedContents("안녕하세요. 테스트 내용입니다."))
                    .pullRequestUrl(new PullRequestUrl("https://github.com"))
                    .deadline(new Deadline(LocalDateTime.now().plusHours(100)))
                    .watchedCount(new WatchedCount(0))
                    .reviewStatus(reviewStatus)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build();

            // when & then
            assertThatThrownBy(() -> runnerPost.updateReviewStatus(reviewStatus))
                    .isInstanceOf(RunnerPostDomainException.class);
        }
    }
}
