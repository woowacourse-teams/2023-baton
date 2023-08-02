package touch.baton.domain.feedback.exception;

import touch.baton.domain.common.exception.BusinessException;

public class FeedbackBusinessException extends BusinessException {

    public FeedbackBusinessException(final String message) {
        super(message);
    }

    public static class NotFoundRunner extends FeedbackBusinessException {

        public NotFoundRunner(final String message) {
            super(message);
        }
    }

    public static class NotFoundSupporter extends FeedbackBusinessException {

        public NotFoundSupporter(final String message) {
            super(message);
        }
    }

    public static class NotFoundRunnerPost extends FeedbackBusinessException {

        public NotFoundRunnerPost(final String message) {
            super(message);
        }
    }

    public static class NotOwner extends FeedbackBusinessException {

        public NotOwner(final String message) {
            super(message);
        }
    }

    public static class DifferentSupporter extends FeedbackBusinessException {

        public DifferentSupporter(final String message) {
            super(message);
        }
    }
}
