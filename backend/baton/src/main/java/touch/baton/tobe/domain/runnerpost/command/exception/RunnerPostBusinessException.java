package touch.baton.tobe.domain.runnerpost.command.exception;

import touch.baton.domain.common.exception.BusinessException;

public class RunnerPostBusinessException extends BusinessException {

    public RunnerPostBusinessException(final String message) {
        super(message);
    }
}
