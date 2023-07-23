package touch.baton.domain.runnerpost.exception;

import touch.baton.domain.runner.exception.OldRunnerException;

public class OldRunnerPostBusinessException extends OldRunnerException {

    public OldRunnerPostBusinessException(final String message) {
        super(message);
    }

    public static class NotFound extends OldRunnerPostBusinessException {

        public NotFound(final String message) {
            super(message);
        }
    }
}
