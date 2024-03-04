package edu.java.bot.baseValidators;

import edu.java.bot.model.validators.baseValidators.StackOverflowValidator;
import org.junit.jupiter.api.Test;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackOverflowValidatorTest {
    @Test
    public void isValidTest() {
        StackOverflowValidator validator = new StackOverflowValidator();
        URI validTestCase = URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        URI invalidTestCase = URI.create("https://github.com/");
        assertTrue(validator.isValid(validTestCase));
        assertFalse(validator.isValid(invalidTestCase));
    }
}
