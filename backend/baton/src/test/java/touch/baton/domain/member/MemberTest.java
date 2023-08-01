package touch.baton.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.member.exception.MemberDomainException;
import touch.baton.domain.member.exception.MemberException;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> Member.builder()
                    .memberName(new MemberName("헤에디주"))
                    .email(new Email("test@test.co.kr"))
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(new Company("우아한형제들"))
                    .imageUrl(new ImageUrl("https://"))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("이름에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_name_is_null() {
            assertThatThrownBy(() -> Member.builder()
                    .memberName(null)
                    .email(new Email("test@test.co.kr"))
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(new Company("우아한형제들"))
                    .imageUrl(new ImageUrl("imageUrl"))
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 name 은 null 일 수 없습니다.");
        }

        @DisplayName("이메일에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_email_is_null() {
            assertThatThrownBy(() -> Member.builder()
                    .memberName(new MemberName("에단"))
                    .email(null)
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(new Company("우아한형제들"))
                    .imageUrl(new ImageUrl("imageUrl"))
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 email 은 null 일 수 없습니다.");
        }

        @DisplayName("oauth id 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_oauth_id_is_null() {
            assertThatThrownBy(() -> Member.builder()
                    .memberName(new MemberName("에단"))
                    .email(new Email("test@test.co.kr"))
                    .oauthId(null)
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(new Company("우아한형제들"))
                    .imageUrl(new ImageUrl("imageUrl"))
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 oauthId 는 null 일 수 없습니다.");
        }

        @DisplayName("github url 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_github_url_is_null() {
            assertThatThrownBy(() -> Member.builder()
                    .memberName(new MemberName("에단"))
                    .email(new Email("test@test.co.kr"))
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(null)
                    .company(new Company("우아한형제들"))
                    .imageUrl(new ImageUrl("imageUrl"))
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 githubUrl 은 null 일 수 없습니다.");
        }

        @DisplayName("company 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_company_is_null() {
            assertThatThrownBy(() -> Member.builder()
                    .memberName(new MemberName("에단"))
                    .email(new Email("test@test.co.kr"))
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(null)
                    .imageUrl(new ImageUrl("imageUrl"))
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 company 는 null 일 수 없습니다.");
        }

        @DisplayName("imageUrl 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_imageUrl_is_null() {
            assertThatThrownBy(() -> Member.builder()
                    .memberName(new MemberName("에단"))
                    .email(new Email("test@test.co.kr"))
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(new Company("우아한형제들"))
                    .imageUrl(null)
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 imageUrl 은 null 일 수 없습니다.");
        }
    }
}
