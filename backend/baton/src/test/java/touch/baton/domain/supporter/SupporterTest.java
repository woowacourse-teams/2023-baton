package touch.baton.domain.supporter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.Name;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.supporter.exception.SupporterException;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SupporterTest {


    @DisplayName("생성 테스트")
    @Nested
    class Create {

        private final Member member = Member.builder()
                .name(new Name("헤에디주"))
                .email(new Email("test@test.co.kr"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .build();

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("startCount 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_startCount_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(null)
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .build()
            ).isInstanceOf(SupporterException.NotNull.class);
        }

        @DisplayName("totalRating 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_totalRating_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(null)
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .build()
            ).isInstanceOf(SupporterException.NotNull.class);
        }

        @DisplayName("grade 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_grade_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(new TotalRating(100))
                    .grade(null)
                    .member(member)
                    .build()
            ).isInstanceOf(SupporterException.NotNull.class);
        }

        @DisplayName("member 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(null)
                    .build()
            ).isInstanceOf(SupporterException.NotNull.class);
        }
    }
}
