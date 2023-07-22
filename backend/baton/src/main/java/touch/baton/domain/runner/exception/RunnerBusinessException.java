package touch.baton.domain.runner.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ErrorCode;

public class RunnerBusinessException extends BusinessException {

    public RunnerBusinessException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
