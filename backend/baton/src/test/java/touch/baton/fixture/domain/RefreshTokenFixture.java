package touch.baton.fixture.domain;

import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.token.ExpireDate;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;

public abstract class RefreshTokenFixture {

    private RefreshTokenFixture() {
    }

    public static RefreshToken create(final Member member, final Token refreshToken, final ExpireDate expireDate) {
        return RefreshToken.builder()
                .member(member)
                .token(refreshToken)
                .expireDate(expireDate)
                .build();
    }
}
