package touch.baton.tobe.domain.oauth.command.exception;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;

public class OauthRequestException extends ClientRequestException {

    public OauthRequestException(final ClientErrorCode errorCode) {
        super(errorCode);
    }
}
