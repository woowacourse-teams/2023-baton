package touch.baton.domain.oauth.token.exception;

import touch.baton.domain.common.exception.DomainException;

public class RefreshTokenDomainException extends DomainException {

    public RefreshTokenDomainException(final String message) {
        super(message);
    }
}
