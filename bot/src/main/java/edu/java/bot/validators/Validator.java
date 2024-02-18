package edu.java.bot.validators;

import java.net.URI;

public interface Validator {
    String name();

    default boolean isValid(URI uri) {
        return uri.getHost() != null && uri.getHost().equals(name()) && !uri.getPath().isEmpty();
    }
}
