package touch.baton.domain.tag.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ErrorCode;

public class TagDomainException extends DomainException {

    public TagDomainException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
