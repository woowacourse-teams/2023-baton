package touch.baton.domain.runnerpost.exception;

import touch.baton.domain.common.exception.DomainException;

public class RunnerPostException extends DomainException {

    public RunnerPostException(final String message) {
        super(message);
    }

    public static class NotNull extends RunnerPostException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
