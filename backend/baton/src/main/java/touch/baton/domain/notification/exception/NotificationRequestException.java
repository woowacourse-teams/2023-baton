package touch.baton.domain.notification.exception;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class NotificationRequestException extends ClientRequestException {

    public NotificationRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
