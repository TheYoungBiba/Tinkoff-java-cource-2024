package edu.java.scrapper.model.DTO;

public record ApiErrorResponse (
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    String[] stacktrace
) {}
