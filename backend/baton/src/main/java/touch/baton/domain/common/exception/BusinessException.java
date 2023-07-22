package touch.baton.domain.common.exception;

public abstract class BusinessException extends BaseException {

    public BusinessException(final ServerErrorCode errorCode) {
        super(errorCode);
    }
}
