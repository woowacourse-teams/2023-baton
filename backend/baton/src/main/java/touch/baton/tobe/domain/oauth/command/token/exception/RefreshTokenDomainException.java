package touch.baton.tobe.domain.oauth.command.token.exception;

import touch.baton.domain.common.exception.DomainException;

public class RefreshTokenDomainException extends DomainException {

    public RefreshTokenDomainException(final String message) {
        super(message);
    }
}
