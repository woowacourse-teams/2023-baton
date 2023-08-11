package touch.baton.domain.runner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.runner.exception.RunnerDomainException;
import touch.baton.domain.technicaltag.RunnerTechnicalTags;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RunnerTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        private final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("imageUrl"))
                .build();

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> Runner.builder()
                    .introduction(new Introduction("안녕하세요. 헤에디주입니다."))
                    .member(member)
                    .runnerTechnicalTags(new RunnerTechnicalTags(new ArrayList<>()))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("member 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> Runner.builder()
                    .introduction(new Introduction("안녕하세요. 헤에디주입니다."))
                    .member(null)
                    .runnerTechnicalTags(new RunnerTechnicalTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(RunnerDomainException.class)
                    .hasMessage("Runner 의 member 는 null 일 수 없습니다.");
        }

        @DisplayName("runnerTechnicalTags 가 null 이 들어갈 경우 예외가 발생하지 않는다.")
        @Test
        void fail_if_runnerTechnicalTags_is_null() {
            assertThatCode(() -> Runner.builder()
                    .introduction(new Introduction("안녕하세요. 헤에디주입니다."))
                    .member(member)
                    .runnerTechnicalTags(null)
                    .build()
            ).doesNotThrowAnyException();
        }
    }
}
