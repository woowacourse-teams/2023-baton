package touch.baton.domain.member.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class MemberBusinessException extends BusinessException {

    public MemberBusinessException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
