package touch.baton.domain.runner.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class RunnerBusinessException extends BusinessException {

    public RunnerBusinessException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
