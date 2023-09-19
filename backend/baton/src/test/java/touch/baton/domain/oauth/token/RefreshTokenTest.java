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

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static touch.baton.domain.oauth.token.RefreshToken.builder;

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
            assertThatCode(() -> builder()
                    .member(owner)
                    .token(new Token("refresh-token"))
                    .expireDate(new ExpireDate(now().plusDays(30)))
                    .build());
        }

        @DisplayName("사용자가 null 이면 실패한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .member(null)
                    .token(new Token("refresh-token"))
                    .expireDate(new ExpireDate(now().plusDays(30)))
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }

        @DisplayName("토큰이 null 이면 실패한다.")
        @Test
        void fail_if_token_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .member(owner)
                    .token(null)
                    .expireDate(new ExpireDate(now().plusDays(30)))
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }

        @DisplayName("만료일이 null 이면 실패한다.")
        @Test
        void fail_if_expireDate_is_null() {
            assertThatThrownBy(() -> RefreshToken.builder()
                    .member(owner)
                    .token(new Token("refresh-token"))
                    .expireDate(null)
                    .build()
            ).isInstanceOf(RefreshTokenDomainException.class);
        }
    }

    @DisplayName("토큰을 업데이트하면 토큰의 내용과 마감기한이 바뀐다.")
    @Test
    void updateToken() {
        // given
        final LocalDateTime currentTime = now();
        final ExpireDate expectedExpireDate = new ExpireDate(currentTime);
        final RefreshToken refreshToken = builder()
                .member(owner)
                .token(new Token("refresh-token"))
                .expireDate(expectedExpireDate)
                .build();

        // when
        final Token updateToken = new Token("update-token");
        refreshToken.updateToken(updateToken, 30);

        // then
        assertAll(
                () -> assertThat(refreshToken.getToken()).isEqualTo(updateToken),
                () -> assertThat(refreshToken.getExpireDate()).isEqualTo(new ExpireDate(currentTime.plusMinutes(30)))
        );
    }

    @DisplayName("토큰의 주인을 확인할 수 있다.")
    @Test
    void isNotOwner() {
        // given
        final LocalDateTime currentTime = now();
        final ExpireDate expectedExpireDate = new ExpireDate(currentTime);
        final RefreshToken refreshToken = builder()
                .member(owner)
                .token(new Token("refresh-token"))
                .expireDate(expectedExpireDate)
                .build();

        final Member notOwner = Member.builder()
                .memberName(new MemberName("Not Owner"))
                .socialId(new SocialId("notOwnerSocialId"))
                .oauthId(new OauthId("notOwnerOauthId"))
                .githubUrl(new GithubUrl("github.com/notOwner"))
                .company(new Company("우아한테크코스"))
                .imageUrl(new ImageUrl("김석호"))
                .build();

        // when, then
        assertAll(
                () -> assertThat(refreshToken.isNotOwner(owner)).isFalse(),
                () -> assertThat(refreshToken.isNotOwner(notOwner)).isTrue()
        );
    }

    @DisplayName("만료되었는지 확인한다.")
    @Test
    void isExpired() {
        // given
        final LocalDateTime currentTime = now().minusDays(20);
        final ExpireDate expectedExpireDate = new ExpireDate(currentTime);
        final RefreshToken refreshToken = builder()
                .member(owner)
                .token(new Token("refresh-token"))
                .expireDate(expectedExpireDate)
                .build();

        final Member notOwner = Member.builder()
                .memberName(new MemberName("Not Owner"))
                .socialId(new SocialId("notOwnerSocialId"))
                .oauthId(new OauthId("notOwnerOauthId"))
                .githubUrl(new GithubUrl("github.com/notOwner"))
                .company(new Company("우아한테크코스"))
                .imageUrl(new ImageUrl("김석호"))
                .build();

        // when, then
        assertThat(refreshToken.isExpired()).isTrue();
    }
}
