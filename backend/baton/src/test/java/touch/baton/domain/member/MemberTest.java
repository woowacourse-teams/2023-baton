package touch.baton.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.member.exception.MemberDomainException;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.fixture.domain.MemberFixture;

import static org.assertj.core.api.Assertions.*;

class MemberTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> Member.builder()
                    .memberName(new MemberName("헤에디주"))
                    .socialId(new SocialId("testSocialId"))
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
                    .socialId(new SocialId("testSocialId"))
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(new Company("우아한형제들"))
                    .imageUrl(new ImageUrl("imageUrl"))
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 name 은 null 일 수 없습니다.");
        }

        @DisplayName("socialId에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_socialId_is_null() {
            assertThatThrownBy(() -> Member.builder()
                    .memberName(new MemberName("에단"))
                    .socialId(null)
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(new Company("우아한형제들"))
                    .imageUrl(new ImageUrl("imageUrl"))
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 socialId 은 null 일 수 없습니다.");
        }

        @DisplayName("oauth id 에 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_oauth_id_is_null() {
            assertThatThrownBy(() -> Member.builder()
                    .memberName(new MemberName("에단"))
                    .socialId(new SocialId("testSocialId"))
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
                    .socialId(new SocialId("testSocialId"))
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
                    .socialId(new SocialId("testSocialId"))
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
                    .socialId(new SocialId("testSocialId"))
                    .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                    .githubUrl(new GithubUrl("github.com/hyena0608"))
                    .company(new Company("우아한형제들"))
                    .imageUrl(null)
                    .build()
            ).isInstanceOf(MemberDomainException.class)
                    .hasMessage("Member 의 imageUrl 은 null 일 수 없습니다.");
        }
    }

    @DisplayName("수정 테스트")
    @Nested
    class Update {

        private Member member;

        @BeforeEach
        void setUp() {
            member = MemberFixture.createDitoo();
        }

        @DisplayName("이름 수정에 성공한다.")
        @Test
        void name_success() {
            // given
            final MemberName updatedName = new MemberName("디투랜드");

            // when
            member.updateMemberName(updatedName);

            // then
            assertThat(member.getMemberName()).isEqualTo(updatedName);
        }

        @DisplayName("이름이 null 이면 이름 수정에 실패한다.")
        @Test
        void name_fail_if_null() {
            assertThatThrownBy(() -> member.updateMemberName(null))
                    .isInstanceOf(MemberDomainException.class);
        }

        @DisplayName("소속 수정에 성공한다.")
        @Test
        void company_success() {
            // given
            final Company updatedCompany = new Company("넥슨");

            // when
            member.updateCompany(updatedCompany);

            // then
            assertThat(member.getCompany()).isEqualTo(updatedCompany);
        }

        @DisplayName("소속이 null 이면 소속 수정에 실패한다.")
        @Test
        void company_fail_if_null() {
            assertThatThrownBy(() -> member.updateCompany(null))
                    .isInstanceOf(MemberDomainException.class);
        }
    }
}
