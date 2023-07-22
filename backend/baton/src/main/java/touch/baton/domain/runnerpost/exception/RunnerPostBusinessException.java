package touch.baton.domain.runnerpost.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ErrorCode;

public class RunnerPostBusinessException extends BusinessException {

    public RunnerPostBusinessException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
