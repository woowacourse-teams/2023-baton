package touch.baton.infra.exception;

import touch.baton.domain.common.exception.BaseException;

public class InfraException extends BaseException {

    public InfraException(final String message) {
        super(message);
    }
}
