package touch.baton.domain.supporter.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ErrorCode;

public class SupporterDomainException extends DomainException {

    public SupporterDomainException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
