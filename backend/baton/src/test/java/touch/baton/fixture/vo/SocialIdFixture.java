package touch.baton.fixture.vo;

import touch.baton.domain.member.vo.SocialId;

public abstract class SocialIdFixture {

    private SocialIdFixture() {
    }

    public static SocialId socialId(final String value) {
        return new SocialId(value);
    }
}
