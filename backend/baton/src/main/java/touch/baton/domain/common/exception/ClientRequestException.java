package touch.baton.domain.common.exception;

public class ClientRequestException extends BaseException {

    private final ClientErrorCode errorCode;

    public ClientRequestException(final ClientErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ClientErrorCode getErrorCode() {
        return errorCode;
    }
}
