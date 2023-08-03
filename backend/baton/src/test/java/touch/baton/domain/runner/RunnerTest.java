package touch.baton.domain.runner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.exception.RunnerDomainException;

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
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("totalRating 이 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_totalRating_is_null() {
            assertThatThrownBy(() -> Runner.builder()
                    .totalRating(null)
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .build()
            ).isInstanceOf(RunnerDomainException.class)
                    .hasMessage("Runner 의 totalRating 은 null 일 수 없습니다.");
        }

        @DisplayName("grade 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_grade_is_null() {
            assertThatThrownBy(() -> Runner.builder()
                    .totalRating(new TotalRating(100))
                    .grade(null)
                    .member(member)
                    .build()
            ).isInstanceOf(RunnerDomainException.class)
                    .hasMessage("Runner 의 grade 는 null 일 수 없습니다.");
        }

        @DisplayName("member 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> Runner.builder()
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(null)
                    .build()
            ).isInstanceOf(RunnerDomainException.class)
                    .hasMessage("Runner 의 member 는 null 일 수 없습니다.");
        }
    }
}
