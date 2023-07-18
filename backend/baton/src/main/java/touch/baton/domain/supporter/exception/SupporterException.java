package touch.baton.domain.supporter.exception;

import touch.baton.domain.common.exception.DomainException;

public class SupporterException extends DomainException {

    public SupporterException(final String message) {
        super(message);
    }

    public static class NotNull extends SupporterException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
