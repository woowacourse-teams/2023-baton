package touch.baton.domain.supporter.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ErrorCode;

public class SupporterBusinessException extends BusinessException {

    public SupporterBusinessException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
