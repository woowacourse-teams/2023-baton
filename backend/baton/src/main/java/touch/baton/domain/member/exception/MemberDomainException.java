package touch.baton.domain.member.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ErrorCode;

public class MemberDomainException extends DomainException {

    public MemberDomainException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
