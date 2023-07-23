package touch.baton.domain.member.exception;

import touch.baton.domain.common.exception.DomainException;
import touch.baton.domain.common.exception.ServerErrorCode;

public class MemberDomainException extends DomainException {

    public MemberDomainException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
