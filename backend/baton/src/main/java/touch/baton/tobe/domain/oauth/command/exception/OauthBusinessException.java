package touch.baton.tobe.domain.oauth.command.exception;

import touch.baton.domain.common.exception.BusinessException;

public class OauthBusinessException extends BusinessException {

    public OauthBusinessException(final String message) {
        super(message);
    }
}
