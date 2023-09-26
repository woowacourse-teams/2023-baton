package touch.baton.tobe.domain.member.exception;

import touch.baton.domain.common.exception.BusinessException;

public class MemberBusinessException extends BusinessException {

    public MemberBusinessException(final String message) {
        super(message);
    }
}
