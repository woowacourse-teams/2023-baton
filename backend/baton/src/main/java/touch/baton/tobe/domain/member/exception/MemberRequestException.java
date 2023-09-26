package touch.baton.tobe.domain.member.exception;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class MemberRequestException extends ClientRequestException {

    public MemberRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
