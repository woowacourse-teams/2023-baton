package touch.baton.tobe.domain.feedback.exception;

import touch.baton.domain.common.exception.BusinessException;

public class FeedbackBusinessException extends BusinessException {

    public FeedbackBusinessException(final String message) {
        super(message);
    }
}
