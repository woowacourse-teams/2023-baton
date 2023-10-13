package touch.baton.fixture.vo;

import touch.baton.domain.member.command.vo.MemberName;

public abstract class MemberNameFixture {

    private MemberNameFixture() {
    }

    public static MemberName memberName(final String value) {
        return new MemberName(value);
    }
}
