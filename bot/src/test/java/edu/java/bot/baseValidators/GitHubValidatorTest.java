package edu.java.bot.baseValidators;

import edu.java.bot.model.validators.baseValidators.GitHubValidator;
import org.junit.jupiter.api.Test;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubValidatorTest {
    @Test
    public void isValidTest() {
        GitHubValidator validator = new GitHubValidator();
        URI validTestCase = URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/");
        URI invalidTestCase = URI.create("/sanyarnd/tinkoff-java-course-2023/");
        assertTrue(validator.isValid(validTestCase));
        assertFalse(validator.isValid(invalidTestCase));
    }
}
