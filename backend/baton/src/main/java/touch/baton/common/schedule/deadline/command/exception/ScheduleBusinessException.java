package touch.baton.common.schedule.deadline.command.exception;

import touch.baton.domain.common.exception.BusinessException;

public class ScheduleBusinessException extends BusinessException {

    public ScheduleBusinessException(final String message) {
        super(message);
    }
}
