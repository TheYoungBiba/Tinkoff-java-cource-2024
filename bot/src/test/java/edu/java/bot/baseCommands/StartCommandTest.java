package edu.java.bot.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.baseCommands.StartCommand;
import edu.java.bot.core.UserContext;
import edu.java.bot.database.InMemoryDatabase;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartCommandTest {
    private final long testUserId = 123;
    private final long testChatId = testUserId;
    private final String testName = "test name";
    private final InMemoryDatabase testDatabaseConnection = new InMemoryDatabase();
    private final StartCommand startCommand = new StartCommand(testDatabaseConnection, "test_name_bot");

    @AfterEach
    public void resetDatabase() {
        testDatabaseConnection.dropDatabase();
    }

    @Test
    public void withoutRegistrationHandleTest() {
        UserContext testCase = new UserContext(testChatId, testUserId, testName, "/start");
        String referent =
            "Hi, test name! You successfully registered in test_name_bot. To add links for tracking send /track";
        SendMessage testResult = startCommand.handle(testCase);
        assertEquals(referent, testResult.getParameters().get("text"));
    }

    @Test
    public void handleTest() {
        UserContext testCase = new UserContext(testChatId, testUserId, testName, "/start");
        testDatabaseConnection.addUser(testCase.userId());
        String referent = "test name, you already registered in test_name_bot. To see your tracklist send /list";
        SendMessage testResult = startCommand.handle(testCase);
        assertEquals(referent, testResult.getParameters().get("text"));
    }
}
