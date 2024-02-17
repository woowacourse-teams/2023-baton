package touch.baton.domain.oauth.command.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.vo.Company;
import touch.baton.domain.member.command.vo.GithubUrl;
import touch.baton.domain.member.command.vo.ImageUrl;
import touch.baton.domain.member.command.vo.MemberName;
import touch.baton.domain.member.command.vo.OauthId;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.command.token.exception.RefreshTokenDomainException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class RefreshTokenTest {

    private static final Member owner = Member.builder()
            .memberName(new MemberName("러너 사용자"))
            .socialId(new SocialId("testSocialId"))
            .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
            .githubUrl(new GithubUrl("github.com/hyena0608"))
            .company(new Company("우아한테크코스"))
            .imageUrl(new ImageUrl("김석호"))
            .build();

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> RefreshToken.builder()
                    .socialId(owner.getSocialId().getValue())
                    .member(owner)
                    .token(new Token("refresh-token"))
                    .timeout(30L)
                    .build());
        }

        @DisplayName("SocialId가 null 이면 실패한다.")
        @Test
        void fail_if_socialId_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .socialId(null)
                    .member(owner)
                    .token(new Token("refresh-token"))
                    .timeout(30L)
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }

        @DisplayName("사용자가 null 이면 실패한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .socialId(owner.getSocialId().getValue())
                    .member(null)
                    .token(new Token("refresh-token"))
                    .timeout(30L)
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }

        @DisplayName("토큰이 null 이면 실패한다.")
        @Test
        void fail_if_token_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .socialId(owner.getSocialId().getValue())
                    .member(owner)
                    .token(null)
                    .timeout(30L)
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }

        @DisplayName("만료일이 null 이면 실패한다.")
        @Test
        void fail_if_expireDate_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .socialId(owner.getSocialId().getValue())
                    .member(owner)
                    .token(new Token("refresh-token"))
                    .timeout(null)
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }
    }

    @DisplayName("토큰의 주인을 확인할 수 있다.")
    @Test
    void isNotOwner() {
        // given
        final RefreshToken refreshToken = RefreshToken.builder()
                .socialId(owner.getSocialId().getValue())
                .member(owner)
                .token(new Token("owner-refresh-token"))
                .timeout(30L)
                .build();

        final Token notOwnerRefreshToken = new Token("not-owner-refresh-token");

        // when, then
        assertAll(
                () -> assertThat(refreshToken.isNotOwner(refreshToken.getToken())).isFalse(),
                () -> assertThat(refreshToken.isNotOwner(notOwnerRefreshToken)).isTrue()
        );
    }
}
