package touch.baton.domain.oauth.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.oauth.token.exception.RefreshTokenDomainException;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static touch.baton.domain.oauth.token.RefreshToken.builder;

class RefreshTokenTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        final Member member = Member.builder()
                .memberName(new MemberName("러너 사용자"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한테크코스"))
                .imageUrl(new ImageUrl("김석호"))
                .build();

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> builder()
                    .member(member)
                    .token(new Token("refresh-token"))
                    .expireDate(new ExpireDate(now().plusHours(30)))
                    .build());
        }

        @DisplayName("사용자가 null 이면 실패한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .member(null)
                    .token(new Token("refresh-token"))
                    .expireDate(new ExpireDate(now().plusHours(30)))
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }

        @DisplayName("토큰이 null 이면 실패한다.")
        @Test
        void fail_if_token_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .member(member)
                    .token(null)
                    .expireDate(new ExpireDate(now().plusHours(30)))
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }

        @DisplayName("만료일이 null 이면 실패한다.")
        @Test
        void fail_if_expireDate_is_null() {
            assertThatThrownBy(() -> builder()
                    .member(member)
                    .token(new Token("refresh-token"))
                    .expireDate(null)
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }
    }
}
