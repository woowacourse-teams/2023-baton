package touch.baton.domain.common.response;

import touch.baton.domain.common.exception.ClientRequestException;

public record ErrorResponse(String errorCode, String message) {

    public static ErrorResponse from(final ClientRequestException e) {
        return new ErrorResponse(e.getErrorCode().getErrorCode(), e.getMessage());
    }
}
