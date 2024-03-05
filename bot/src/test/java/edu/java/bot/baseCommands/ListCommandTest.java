package edu.java.bot.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.baseCommands.ListCommand;
import edu.java.bot.core.UserContext;
import edu.java.bot.database.InMemoryDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCommandTest {
    private final long testUserId = 123;
    private final long testChatId = testUserId;
    private final String testName = "test name";
    private final InMemoryDatabase testDatabaseConnection = new InMemoryDatabase();
    ListCommand listCommand = new ListCommand(testDatabaseConnection);

    @AfterEach
    public void resetDatabase() {
        testDatabaseConnection.dropDatabase();
    }

    @Test
    public void emptyWithoutRegistrationHandleTest() {
        UserContext testCase = getTestContext("/list");
        SendMessage testResult = listCommand.handle(testCase);
        String referent = "You successfully registered! To add links in your tracklist use /track";
        assertEquals(referent, testResult.getParameters().get("text"));
    }

    @Test
    public void emptyHandleTest() {
        UserContext testCase = getTestContext("/list");
        testDatabaseConnection.addUser(testCase.userId(), List.of(
            URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
            URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
            URI.create("https://stackoverflow.com/search?q=unsupported%20link")
        ));
        SendMessage testResult = listCommand.handle(testCase);
        String referent = """
            test name's tracklist:
            · https://github.com/sanyarnd/tinkoff-java-course-2023/
            · https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            · https://stackoverflow.com/search?q=unsupported%20link
            """;
        assertEquals(referent, testResult.getParameters().get("text"));

    }

    private UserContext getTestContext(String testCase) {
        return new UserContext(testChatId, testUserId, testName, testCase);
    }
}
