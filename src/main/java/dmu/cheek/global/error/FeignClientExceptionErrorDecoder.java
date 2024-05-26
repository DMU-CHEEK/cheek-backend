package dmu.cheek.global.error;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {

    private ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response respose) {
        log.error("{} 요청 실패", methodKey);
        log.error("status: {}", respose.status());
        log.error("reason: {}", respose.reason());

        FeignException exception = FeignException.errorStatus(methodKey, respose);
        HttpStatus status = HttpStatus.valueOf(respose.status());

        if (status.is5xxServerError()) {
            return new RetryableException(
                    respose.status(),
                    exception.getMessage(),
                    respose.request().httpMethod(),
                    exception,
                    (Long) null,
                    respose.request()
            );
        }
        return errorDecoder.decode(methodKey, respose);
    }
}
