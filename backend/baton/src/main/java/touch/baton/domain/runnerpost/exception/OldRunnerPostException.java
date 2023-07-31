package touch.baton.domain.runnerpost.exception;

public class OldRunnerPostException extends RuntimeException {

    public OldRunnerPostException(final String message) {
        super(message);
    }

    public static class NotNull extends OldRunnerPostException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
