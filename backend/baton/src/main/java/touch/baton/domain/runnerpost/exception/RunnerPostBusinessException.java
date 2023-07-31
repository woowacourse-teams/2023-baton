package touch.baton.domain.runnerpost.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class RunnerPostBusinessException extends BusinessException {

    public RunnerPostBusinessException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
