package dmu.cheek.global.error;

import dmu.cheek.global.error.exception.BusinessException;
import dmu.cheek.global.error.exception.InProgressException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Business Exception
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessException(BusinessException businessException) {
        log.error("BusinessException", businessException);
        ErrorResponse response = ErrorResponse.of(businessException.getErrorCode().getErrorCode(), businessException.getMessage());

        return ResponseEntity.status(businessException.getErrorCode().getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(InProgressException.class)
    public ResponseEntity<ErrorResponse> inProgressException(InProgressException exception) {

        log.error("exception", exception);

        ErrorResponse response = ErrorResponse.of(exception.getErrorCode().getErrorCode(), exception.getMessage());

        return ResponseEntity.status(exception.getErrorCode().getHttpStatus())
                .body(response);
    }

    /**
     * Http Request Not Supported Exception
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        log.error("HttpRequestMethodNotSupportedException", httpRequestMethodNotSupportedException);
        ErrorResponse response = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED.toString(), httpRequestMethodNotSupportedException.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(response);
    }

    /**
     * Bind Exception
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException bindException) {
        log.error("BindException", bindException);
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), bindException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Checked Exception
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException runtimeException) {
        log.error("RuntimeException", runtimeException);
        ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), runtimeException.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    /**
     * Unchecked Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> checkedException(Exception exception) {
        log.error("Exception", exception);
        ErrorResponse response = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR.toString(), exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
