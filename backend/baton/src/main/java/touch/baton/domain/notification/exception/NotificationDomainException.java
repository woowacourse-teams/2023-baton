package touch.baton.domain.notification.exception;

import touch.baton.domain.common.exception.DomainException;

public class NotificationDomainException extends DomainException {

    public NotificationDomainException(final String message) {
        super(message);
    }
}
