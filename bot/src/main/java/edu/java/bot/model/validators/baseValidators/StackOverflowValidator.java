package edu.java.bot.model.validators.baseValidators;

import edu.java.bot.model.validators.Validator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StackOverflowValidator implements Validator {
    @Override
    public String name() {
        return "stackoverflow.com";
    }
}
