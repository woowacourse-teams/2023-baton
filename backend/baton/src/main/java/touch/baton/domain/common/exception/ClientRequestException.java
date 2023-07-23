package touch.baton.domain.common.exception;

public abstract class ClientRequestException extends BaseException {

    public ClientRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
