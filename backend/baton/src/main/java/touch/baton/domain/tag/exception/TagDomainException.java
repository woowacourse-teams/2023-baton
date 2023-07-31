package touch.baton.domain.tag.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class TagDomainException extends DomainException {

    public TagDomainException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
