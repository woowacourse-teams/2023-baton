package touch.baton.domain.runnerpost;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.ChattingRoomCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.exception.RunnerPostException;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.tag.RunnerPostTags;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerPostTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

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
                .build();

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> RunnerPost.builder()
                    .title(new Title("JPA 정복"))
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingRoomCount(new ChattingRoomCount(0))
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("supporter 에 null 이 들어간 경우 아직 리뷰어 할당이 되지 않은 것이다.")
        @Test
        void success_if_supporter_is_null() {
            assertThatCode(() -> RunnerPost.builder()
                    .title(new Title("아이"))
                    .contents(new Contents("김영한 짱짱맨"))
                    .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                    .deadline(new Deadline(LocalDateTime.now()))
                    .watchedCount(new WatchedCount(0))
                    .chattingRoomCount(new ChattingRoomCount(0))
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
                    .chattingRoomCount(new ChattingRoomCount(0))
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostException.NotNull.class);
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
                    .chattingRoomCount(new ChattingRoomCount(0))
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostException.NotNull.class);
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
                    .chattingRoomCount(new ChattingRoomCount(0))
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostException.NotNull.class);
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
                    .chattingRoomCount(new ChattingRoomCount(0))
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostException.NotNull.class);
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
                    .chattingRoomCount(new ChattingRoomCount(0))
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostException.NotNull.class);
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
                    .chattingRoomCount(null)
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostException.NotNull.class);
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
                    .chattingRoomCount(new ChattingRoomCount(0))
                    .runner(null)
                    .supporter(supporter)
                    .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerPostException.NotNull.class);
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
                    .chattingRoomCount(new ChattingRoomCount(0))
                    .runner(runner)
                    .supporter(supporter)
                    .runnerPostTags(null)
                    .build()
            ).isInstanceOf(RunnerPostException.NotNull.class);
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
                    () -> assertThat(runnerPost.getChattingRoomCount()).isEqualTo(new ChattingRoomCount(0)),
                    () -> assertThat(runnerPost.getWatchedCount()).isEqualTo(new WatchedCount(0))
            );
        }
    }
}
