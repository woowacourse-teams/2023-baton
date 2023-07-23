package touch.baton.domain.tag.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class TagBusinessException extends BusinessException {

    public TagBusinessException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
