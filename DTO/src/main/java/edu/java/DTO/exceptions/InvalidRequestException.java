package edu.java.DTO.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidRequestException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public InvalidRequestException(String message) {
        super(message);
    }
}
