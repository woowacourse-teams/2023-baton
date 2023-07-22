package touch.baton.domain.common.exception;

public abstract class DomainException extends BaseException {

    public DomainException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
