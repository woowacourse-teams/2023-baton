package touch.baton.infra.exception;

import touch.baton.domain.common.exception.BaseException;

public class InfraException extends BaseException {

    private static final String MESSAGE = "외부 인프라 통신에서 예외가 발생했습니다.";

    public InfraException() {
        super(MESSAGE);
    }
}
