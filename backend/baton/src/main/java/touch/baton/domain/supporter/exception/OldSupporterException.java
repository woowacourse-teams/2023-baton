package touch.baton.domain.supporter.exception;

public class OldSupporterException extends RuntimeException {

    public OldSupporterException(final String message) {
        super(message);
    }

    public static class NotNull extends OldSupporterException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
