package touch.baton.fixture.domain;

import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;

import static touch.baton.fixture.vo.TokenFixture.token;

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

    public static RefreshToken create(final String socialId, final String token, final Member member) {
        return create(socialId, token(token), member, 30L);
    }

    public static RefreshToken ethanRefreshToken() {
        final Member ethan = MemberFixture.createEthan();
        return create(ethan.getSocialId().getValue(), token("ethan-refresh-token"), ethan, 30L);
    }

    public static RefreshToken hyenaRefreshToken() {
        final Member hyena = MemberFixture.createHyena();
        return create(hyena.getSocialId().getValue(), token("hyena-refresh-token"), hyena, 30L);
    }
}
