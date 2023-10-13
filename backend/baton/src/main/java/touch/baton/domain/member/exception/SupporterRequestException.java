package touch.baton.domain.member.exception;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class SupporterRequestException extends ClientRequestException {

    public SupporterRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
