package touch.baton.domain.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import touch.baton.domain.common.exception.ClientRequestException;
import touch.baton.domain.common.response.ErrorResponse;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ClientRequestException.class)
    public ResponseEntity<ErrorResponse> handleClientRequest(ClientRequestException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorResponse.from(e));
    }
}
