package touch.baton.domain.common.exception;

public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(final ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public String getErrorCode() {
        return errorCode.getErrorCode();
    }
}
