package touch.baton.domain.tag.exception;

public class OldTagException extends RuntimeException {

    public OldTagException(final String message) {
        super(message);
    }

    public static class NotNull extends OldTagException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
