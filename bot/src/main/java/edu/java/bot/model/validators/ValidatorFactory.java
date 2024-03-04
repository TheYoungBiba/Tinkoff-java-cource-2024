package edu.java.bot.model.validators;

import java.net.URI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorFactory {
    public static Validator create(String name) {
        return new Validator() {
            @Override
            public String name() {
                return name;
            }
        };
    }

    public static Validator create(String name, String hostName) {
        return new Validator() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public boolean isValid(URI uri) {
                return uri.getHost().equals(hostName) && !uri.getPath().isEmpty();
            }
        };
    }
}
