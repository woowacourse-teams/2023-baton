package touch.baton.domain.runnerpost.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class RunnerPostDomainException extends DomainException {

    public RunnerPostDomainException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
