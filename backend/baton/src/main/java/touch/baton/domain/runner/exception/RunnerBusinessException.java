package touch.baton.domain.runner.exception;

import touch.baton.domain.common.exception.BusinessException;

public class RunnerBusinessException extends BusinessException {

    public RunnerBusinessException(final String message) {
        super(message);
    }
}
