package touch.baton.domain.alarm.exception;

import touch.baton.domain.common.exception.DomainException;

public class AlarmDomainException extends DomainException {

    public AlarmDomainException(final String message) {
        super(message);
    }
}
