package edu.java.bot.validators.baseValidators;

import edu.java.bot.validators.Validator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GitHubValidator implements Validator {
    @Override
    public String name() {
        return "github.com";
    }
}
