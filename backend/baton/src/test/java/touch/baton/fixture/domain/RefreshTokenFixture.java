package touch.baton.fixture.domain;

import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;

public abstract class RefreshTokenFixture {

    private RefreshTokenFixture() {
    }

    public static RefreshToken create(final String socialId, final Token token, final Member member, final Long timeout) {
        return RefreshToken.builder()
                .socialId(socialId)
                .token(token)
                .member(member)
                .timeout(timeout)
                .build();
    }

    public static RefreshToken create(final String socialId, final Token token, final Member member) {
        return create(socialId, token, member, 30L);
    }
}
