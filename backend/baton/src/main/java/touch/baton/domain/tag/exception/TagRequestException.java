package touch.baton.domain.tag.exception;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class TagRequestException extends ClientRequestException {

    public TagRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
