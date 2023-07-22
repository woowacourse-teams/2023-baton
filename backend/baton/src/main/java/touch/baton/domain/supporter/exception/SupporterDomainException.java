package touch.baton.domain.supporter.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class SupporterDomainException extends DomainException {

    public SupporterDomainException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
