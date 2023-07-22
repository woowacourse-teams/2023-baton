package touch.baton.domain.tag.exception;

import org.springframework.http.HttpStatus;
import touch.baton.domain.common.exception.ErrorCode;

public enum TagErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    TagErrorCode(final HttpStatus httpStatus, final String errorCode, final String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
