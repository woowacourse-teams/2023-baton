package touch.baton.domain.supporter.exception;

import org.springframework.http.HttpStatus;
import touch.baton.domain.common.exception.ErrorCode;

public enum SupporterErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    SupporterErrorCode(final HttpStatus httpStatus, final String errorCode, final String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public String getErrorCode() {
        return null;
    }
}
