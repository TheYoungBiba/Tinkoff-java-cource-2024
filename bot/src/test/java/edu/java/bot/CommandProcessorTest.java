package edu.java.bot;

import edu.java.bot.commands.Command;
import edu.java.bot.commands.baseCommands.ListCommand;
import edu.java.bot.commands.baseCommands.StartCommand;
import edu.java.bot.commands.baseCommands.TrackCommand;
import edu.java.bot.commands.baseCommands.UntrackCommand;
import edu.java.bot.core.UserContext;
import edu.java.bot.database.InMemoryDatabase;
import edu.java.bot.core.processors.CommandProcessor;
import edu.java.bot.validators.baseValidators.GitHubValidator;
import edu.java.bot.validators.baseValidators.StackOverflowValidator;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandProcessorTest {
    private final long testUserId = 123;
    private final long testChatId = testUserId;
    private final String testName = "test name";
    private final String testBotName = "test_name_bot";
    private final InMemoryDatabase testDatabaseConnection = new InMemoryDatabase();
    private final List<Command> commands = List.of(
        new ListCommand(testDatabaseConnection),
        new StartCommand(testDatabaseConnection, testBotName),
        new TrackCommand(
            testDatabaseConnection,
            List.of(new GitHubValidator(), new StackOverflowValidator()),
            testBotName
        ),
        new UntrackCommand(testDatabaseConnection, testBotName)
    );
    private final CommandProcessor processor = new CommandProcessor(commands);

    @Test
    public void invalidProcessInputTest() {
        UserContext testCase = new UserContext(testChatId, testUserId, testName, "invalidInput");
        String referent = "No such available command. Use /help to see list of abilities";
        assertEquals(referent, processor.process(testCase).getParameters().get("text"));
    }
}
