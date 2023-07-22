package touch.baton.domain.tag.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ErrorCode;

public class TagBusinessException extends BusinessException {

    public TagBusinessException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
