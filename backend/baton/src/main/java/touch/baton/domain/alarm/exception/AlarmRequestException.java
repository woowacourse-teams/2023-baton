package touch.baton.domain.alarm.exception;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class AlarmRequestException extends ClientRequestException {

    public AlarmRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
