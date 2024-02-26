package touch.baton.common.schedule.deadline.command.exception;

import touch.baton.domain.common.exception.BusinessException;

public class DeadlineOutboxException extends BusinessException {

    public DeadlineOutboxException(final String message) {
        super(message);
    }
}
