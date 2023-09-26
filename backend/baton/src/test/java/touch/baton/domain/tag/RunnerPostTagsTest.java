package touch.baton.domain.tag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.vo.Company;
import touch.baton.tobe.domain.member.command.vo.GithubUrl;
import touch.baton.tobe.domain.member.command.vo.ImageUrl;
import touch.baton.tobe.domain.member.command.vo.MemberName;
import touch.baton.tobe.domain.member.command.vo.OauthId;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.fixture.domain.RunnerTechnicalTagsFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한테크코스"))
                .imageUrl(new ImageUrl("김석호"))
                .build();
        Runner runner = Runner.builder()
                .member(member)
                .runnerTechnicalTags(RunnerTechnicalTagsFixture.create(new ArrayList<>()))
                .build();
        final RunnerPost runnerpost = RunnerPost.newInstance("리뷰해주세요.", "제발요.", "디투가 궁금해요", "참고 해요~", "https://github.com/cookienc", LocalDateTime.of(2099, 12, 12, 0, 0), runner);

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
