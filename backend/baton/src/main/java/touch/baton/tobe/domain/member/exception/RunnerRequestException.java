package touch.baton.tobe.domain.member.exception;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class RunnerRequestException extends ClientRequestException {

    public RunnerRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
