package touch.baton.domain.common.exception;

public class ClientRequestException extends BaseException {

    public ClientRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
