package touch.baton.domain.runner.exception;

import touch.baton.domain.common.exception.DomainException;

public class RunnerException extends DomainException {

    public RunnerException(final String message) {
        super(message);
    }

    public static class NotNull extends RunnerException {

        public NotNull(final String message) {
            super(message);
        }
    }
}
