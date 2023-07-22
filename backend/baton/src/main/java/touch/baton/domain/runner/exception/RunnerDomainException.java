package touch.baton.domain.runner.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ErrorCode;

public class RunnerDomainException extends DomainException {

    public RunnerDomainException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
