package edu.java.scrapper.api.exceptionHandler;

import edu.java.scrapper.model.DTO.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> catchApiException(ApiException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(
            new ApiErrorResponse(
                e.getHttpStatus().name(),
                String.valueOf(e.getHttpStatus().value()),
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)),
            e.getHttpStatus()
        );
    }
}
