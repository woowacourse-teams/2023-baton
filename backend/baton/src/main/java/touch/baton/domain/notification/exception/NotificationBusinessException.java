package touch.baton.domain.notification.exception;

import touch.baton.domain.common.exception.BusinessException;

public class NotificationBusinessException extends BusinessException {

    public NotificationBusinessException(final String message) {
        super(message);
    }
}
