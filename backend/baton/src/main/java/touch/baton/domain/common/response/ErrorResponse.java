package touch.baton.domain.common.response;

import org.springframework.http.HttpStatus;
import touch.baton.domain.common.exception.BaseException;

public class ErrorResponse {

    private final HttpStatus status;
    private final String errorCode;
    private final String message;

    private ErrorResponse(final HttpStatus status, final String errorCode, final String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse from(final BaseException e) {
        return new ErrorResponse(e.getHttpStatus(), e.getErrorCode(), e.getMessage());
    }
}
