package touch.baton.domain.runnerpost;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.ChattingCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Grade;
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
import touch.baton.domain.runnerpost.exception.RunnerPostDomainException;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerPostTest {

    private final Member runnerMember = Member.builder()
            .memberName(new MemberName("러너 사용자"))
            .email(new Email("test@test.co.kr"))
            .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
            .githubUrl(new GithubUrl("github.com/hyena0608"))
            .company(new Company("우아한테크코스"))
            .imageUrl(new ImageUrl("김석호"))
            .build();

    private final Member supporterMember = Member.builder()
            .memberName(new MemberName("서포터 사용자"))
            .email(new Email("test@test.co.kr"))
            .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
            .githubUrl(new GithubUrl("github.com/pobi"))
            .company(new Company("우아한형제들"))
            .imageUrl(new ImageUrl("김석호"))
            .build();

    private final Runner runner = Runner.builder()
            .totalRating(new TotalRating(100))
            .grade(Grade.BARE_FOOT)
            .member(runnerMember)
            .build();

    private final Supporter supporter = Supporter.builder()
            .reviewCount(new ReviewCount(10))
            .starCount(new StarCount(10))
            .totalRating(new TotalRating(100))
            .grade(Grade.BARE_FOOT)
            .member(supporterMember)
            .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
            .build();

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> RunnerPost.builder()
                    .title(new Title("JPA 정복"))
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingCount(new ChattingCount(0))
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
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingCount(new ChattingCount(0))
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
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingCount(new ChattingCount(0))
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
                    .chattingCount(new ChattingCount(0))
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
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(null)
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingCount(new ChattingCount(0))
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
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(null)
                    .watchedCount(new WatchedCount(0))
                    .chattingCount(new ChattingCount(0))
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
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(null)
                    .chattingCount(new ChattingCount(0))
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 watchedCount 는 null 일 수 없습니다.");
        }

        @DisplayName("chatting room count 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_chattingRoomCount_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(new Title("아이"))
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingCount(null)
                    .reviewStatus(ReviewStatus.NOT_STARTED)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostDomainException.class)
                    .hasMessage("RunnerPost 의 chattingCount 는 null 일 수 없습니다.");
        }

        @DisplayName("runner 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_runner_is_null() {
            assertThatThrownBy(() -> RunnerPost.builder()
                    .title(new Title("아이"))
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingCount(new ChattingCount(0))
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
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingCount(new ChattingCount(0))
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
                    () -> assertThat(runnerPost.getContents()).isEqualTo(new Contents(contents)),
                    () -> assertThat(runnerPost.getPullRequestUrl()).isEqualTo(new PullRequestUrl(pullRequestUrl)),
                    () -> assertThat(runnerPost.getDeadline()).isEqualTo(new Deadline(deadline)),
                    () -> assertThat(runnerPost.getRunnerPostTags()).isNotNull(),
                    () -> assertThat(runnerPost.getChattingCount()).isEqualTo(new ChattingCount(0)),
                    () -> assertThat(runnerPost.getWatchedCount()).isEqualTo(new WatchedCount(0))
            );
        }
    }

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

        // when
        runnerPost.addAllRunnerPostTags(List.of(java, spring));

        // then
        assertThat(runnerPost.getRunnerPostTags().getRunnerPostTags()).hasSize(2);
    }
}
