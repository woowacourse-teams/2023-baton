package touch.baton.domain.runnerpost.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ErrorCode;

public class RunnerPostDomainException extends DomainException {

    public RunnerPostDomainException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
