package touch.baton.domain.tag.exception;

import touch.baton.domain.common.exception.DomainException;

public class TagException extends DomainException {

    public TagException(final String message) {
        super(message);
    }

    public static class NotNull extends TagException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
