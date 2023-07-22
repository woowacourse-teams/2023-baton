package touch.baton.domain.runner.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class RunnerDomainException extends DomainException {

    public RunnerDomainException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
