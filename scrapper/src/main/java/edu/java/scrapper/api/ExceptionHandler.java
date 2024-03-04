package edu.java.scrapper.api;

import edu.java.DTO.exceptions.InvalidRequestException;
import edu.java.DTO.exceptions.ResourceNotFoundException;
import edu.java.DTO.resonses.ApiErrorResponse;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> catchInvalidRequestException(InvalidRequestException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(
            new ApiErrorResponse(
                "BAD_REQUEST",
                "400",
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)),
            HttpStatus.BAD_REQUEST
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(
            new ApiErrorResponse(
                "NOT_FOUND",
                "404",
                e.getClass().getName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new)),
            HttpStatus.NOT_FOUND
        );
    }
}
