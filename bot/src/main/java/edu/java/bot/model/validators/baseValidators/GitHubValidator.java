package edu.java.bot.model.validators.baseValidators;

import edu.java.bot.model.validators.Validator;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GitHubValidator implements Validator {
    @Override
    public String name() {
        return "github.com";
    }
}
