package touch.baton.domain.runner.exception;

public class OldRunnerException extends RuntimeException {

    public OldRunnerException(final String message) {
        super(message);
    }

    public static class NotNull extends OldRunnerException {

        public NotNull(final String message) {
            super(message);
        }
    }

    public static class NotFound extends OldRunnerException {

        public NotFound(final String message) {
            super(message);
        }
    }
}
