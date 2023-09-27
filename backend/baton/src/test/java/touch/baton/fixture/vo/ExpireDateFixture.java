package touch.baton.fixture.vo;

import touch.baton.domain.oauth.command.token.ExpireDate;

import java.time.LocalDateTime;

public abstract class ExpireDateFixture {

    private ExpireDateFixture() {
    }

    public static ExpireDate expireDate(final LocalDateTime expireDate) {
        return new ExpireDate(expireDate);
    }
}
