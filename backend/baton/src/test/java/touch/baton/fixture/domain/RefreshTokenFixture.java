package touch.baton.fixture.domain;

import touch.baton.domain.member.Member;
import touch.baton.domain.oauth.token.RefreshToken;
import touch.baton.domain.oauth.token.Token;

public abstract class RefreshTokenFixture {

    private RefreshTokenFixture() {
    }

    public RefreshToken create(final Member member, final Token refreshToken) {
        return RefreshToken.builder()
                .member(member)
                .token(refreshToken)
                .build();
    }
}
