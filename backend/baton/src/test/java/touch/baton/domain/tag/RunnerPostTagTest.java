package touch.baton.domain.tag;

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
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.tag.exception.TagException;
import touch.baton.domain.tag.vo.TagCount;
import touch.baton.domain.tag.vo.TagName;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RunnerPostTagTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

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

        private final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("JPA 정복"))
                .contents(new Contents("김영한 짱짱맨"))
                .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                .deadline(new Deadline(LocalDateTime.now()))
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();

        private Tag tag = Tag.builder()
                .tagName(new TagName("자바"))
                .tagCount(new TagCount(0))
                .build();

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> RunnerPostTag.builder()
                    .runnerPost(runnerPost)
                    .tag(tag)
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("runner post 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_runnerPost_is_null() {
            assertThatThrownBy(() -> RunnerPostTag.builder()
                    .runnerPost(null)
                    .tag(tag)
                    .build()
            ).isInstanceOf(TagException.NotNull.class);
        }

        @DisplayName("tag 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_tag_is_null() {
            assertThatThrownBy(() -> RunnerPostTag.builder()
                    .runnerPost(runnerPost)
                    .tag(null)
                    .build()
            ).isInstanceOf(TagException.NotNull.class);
        }
    }
}
