package touch.baton.domain.runnerpost.command.exception;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class RunnerPostRequestException extends ClientRequestException {

    public RunnerPostRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
