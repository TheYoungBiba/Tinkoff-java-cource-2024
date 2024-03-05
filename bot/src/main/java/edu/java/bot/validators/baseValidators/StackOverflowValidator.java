package edu.java.bot.validators.baseValidators;

import edu.java.bot.validators.Validator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StackOverflowValidator implements Validator {
    @Override
    public String name() {
        return "stackoverflow.com";
    }
}
