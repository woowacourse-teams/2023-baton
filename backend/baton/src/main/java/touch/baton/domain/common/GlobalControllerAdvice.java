package touch.baton.domain.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;
import touch.baton.common.LoggerUtils;
import touch.baton.domain.common.exception.BaseException;
import touch.baton.domain.common.exception.ClientRequestException;
import touch.baton.domain.common.response.ErrorResponse;
import touch.baton.domain.common.response.ServerErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ClientRequestException.class)
    public ResponseEntity<ErrorResponse> handleClientRequest(final HttpServletRequest request,
                                                             final ClientRequestException e
    ) {
        LoggerUtils.logWarn(request, e);
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.from(e));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            MethodArgumentNotValidException.class,
            NoHandlerFoundException.class,
            AsyncRequestTimeoutException.class,
            ErrorResponseException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class})
    public ResponseEntity<ServerErrorResponse> handle(final RuntimeException ex,
                                                      final HttpHeaders headers,
                                                      final HttpStatusCode statusCode,
                                                      final WebRequest request
    ) {
        return ResponseEntity.badRequest().body(ServerErrorResponse.from(ex));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ServerErrorResponse> handleBaseException(final HttpServletRequest httpServletRequest,
                                                                   final BaseException ex,
                                                                   final HttpHeaders headers,
                                                                   final HttpStatusCode statusCode,
                                                                   final WebRequest request
    ) {
        LoggerUtils.logWarn(httpServletRequest, ex);
        return ResponseEntity.internalServerError().body(ServerErrorResponse.from(ex));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex,
                                                             final Object body,
                                                             final HttpHeaders headers,
                                                             final HttpStatusCode statusCode,
                                                             final WebRequest request,
                                                             final HttpServletRequest httpServletRequest
    ) {
        LoggerUtils.logError(httpServletRequest, ex);
        return ResponseEntity.internalServerError().body(ServerErrorResponse.unExpected());
    }
}
