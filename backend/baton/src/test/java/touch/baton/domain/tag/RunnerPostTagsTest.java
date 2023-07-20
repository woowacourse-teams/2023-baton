package touch.baton.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RunnerPostTagsTest {

    @DisplayName("RunnerPostTags 에 runnerPostTag 를 추가할 수 있다.")
    @Test
    void addAllRunnerPostTags() {
        // given
        RunnerPostTags postTags = new RunnerPostTags();
        Member member = Member.builder()
                .memberName(new MemberName("러너 사용자"))
                .email(new Email("test@test.co.kr"))
                .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한테크코스"))
                .imageUrl(new ImageUrl("김석호"))
                .build();
        Runner runner = Runner.builder()
                .totalRating(new TotalRating(100))
                .grade(Grade.BARE_FOOT)
                .member(member)
                .build();
        final RunnerPost runnerpost = RunnerPost.newInstance("리뷰해주세요.", "제발요.", "https://github.com/cookienc", LocalDateTime.of(2099, 12, 12, 0, 0), runner);

        final RunnerPostTag runnerPostTag = RunnerPostTag.builder()
                .runnerPost(runnerpost)
                .tag(Tag.newInstance("Java"))
                .build();

        // when
        postTags.addAll(List.of(runnerPostTag));

        // then
        assertThat(postTags.getRunnerPostTags()).hasSize(1);
    }
}
