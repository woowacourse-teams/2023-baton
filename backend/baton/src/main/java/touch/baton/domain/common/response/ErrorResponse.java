package touch.baton.domain.common.response;

import lombok.Getter;
import touch.baton.domain.common.exception.BaseException;

@Getter
public class ErrorResponse {

    private final String errorCode;
    private final String message;

    private ErrorResponse(final String errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse from(final BaseException e) {
        return new ErrorResponse(e.getErrorCode(), e.getMessage());
    }
}
