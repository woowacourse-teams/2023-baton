package touch.baton.domain.member.exception;

import touch.baton.domain.common.exception.BusinessException;
import touch.baton.domain.common.exception.ErrorCode;

public class MemberBusinessException extends BusinessException {

    public MemberBusinessException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
