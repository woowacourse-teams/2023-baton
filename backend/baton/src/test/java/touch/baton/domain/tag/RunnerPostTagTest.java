package touch.baton.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.tag.exception.RunnerPostTagDomainException;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;

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
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한테크코스"))
                .imageUrl(new ImageUrl("imageUrl"))
                .build();

        private final Member supporterMember = Member.builder()
                .memberName(new MemberName("서포터 사용자"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/pobi"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("imageUrl"))
                .build();

        private final Runner runner = Runner.builder()
                .member(runnerMember)
                .build();

        private final Supporter supporter = Supporter.builder()
                .reviewCount(new ReviewCount(10))
                .member(supporterMember)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();

        private final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("JPA 정복"))
                .contents(new Contents("김영한 짱짱맨"))
                .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                .deadline(new Deadline(LocalDateTime.now()))
                .watchedCount(new WatchedCount(0))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();

        private Tag tag = Tag.builder()
                .tagName(new TagName("자바"))
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
            ).isInstanceOf(RunnerPostTagDomainException.class)
                    .hasMessage("RunnerPostTag 의 runnerPost 는 null 일 수 없습니다.");
        }

        @DisplayName("tag 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_tag_is_null() {
            assertThatThrownBy(() -> RunnerPostTag.builder()
                    .runnerPost(runnerPost)
                    .tag(null)
                    .build()
            ).isInstanceOf(RunnerPostTagDomainException.class)
                    .hasMessage("RunnerPostTag 의 tag 는 null 일 수 없습니다.");
        }
    }
}
