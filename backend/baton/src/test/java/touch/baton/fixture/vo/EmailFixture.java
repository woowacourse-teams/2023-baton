package touch.baton.fixture.vo;

import touch.baton.domain.member.vo.Email;

public abstract class EmailFixture {

    private EmailFixture() {
    }

    public static Email email(final String value) {
        return new Email(value);
    }
}
