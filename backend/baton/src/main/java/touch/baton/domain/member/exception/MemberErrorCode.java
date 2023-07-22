package touch.baton.domain.member.exception;

import org.springframework.http.HttpStatus;
import touch.baton.domain.common.exception.ErrorCode;

public enum MemberErrorCode implements ErrorCode {
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    MemberErrorCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.errorCode = code;
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
