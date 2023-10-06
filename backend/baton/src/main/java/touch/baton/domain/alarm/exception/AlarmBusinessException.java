package touch.baton.domain.alarm.exception;

import touch.baton.domain.common.exception.BusinessException;

public class AlarmBusinessException extends BusinessException {

    public AlarmBusinessException(final String message) {
        super(message);
    }
}
