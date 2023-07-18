package touch.baton.domain.member.exception;

import touch.baton.domain.common.exception.DomainException;

public class MemberException extends DomainException {

    public MemberException(final String message) {
        super(message);
    }

    public static class NotNull extends MemberException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
