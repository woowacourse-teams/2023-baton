package touch.baton.domain.supporter.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class SupporterBusinessException extends BusinessException {

    public SupporterBusinessException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
