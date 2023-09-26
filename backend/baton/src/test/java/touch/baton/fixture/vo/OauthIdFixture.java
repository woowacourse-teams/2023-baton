package touch.baton.fixture.vo;

import touch.baton.tobe.domain.member.command.vo.OauthId;

public abstract class OauthIdFixture {

    private OauthIdFixture() {
    }

    public static OauthId oauthId(final String value) {
        return new OauthId(value);
    }
}
