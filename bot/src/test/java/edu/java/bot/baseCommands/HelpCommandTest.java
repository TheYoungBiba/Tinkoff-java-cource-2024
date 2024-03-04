package edu.java.bot.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.commands.Command;
import edu.java.bot.model.commands.baseCommands.HelpCommand;
import edu.java.bot.model.commands.baseCommands.ListCommand;
import edu.java.bot.model.commands.baseCommands.StartCommand;
import edu.java.bot.model.commands.baseCommands.TrackCommand;
import edu.java.bot.model.commands.baseCommands.UntrackCommand;
import edu.java.bot.model.core.UserContext;
import edu.java.bot.database.InMemoryDatabase;
import edu.java.bot.model.validators.baseValidators.GitHubValidator;
import edu.java.bot.model.validators.baseValidators.StackOverflowValidator;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelpCommandTest {
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
    private final HelpCommand helpCommand = new HelpCommand(commands);

    @Test
    public void handleTest() {
        UserContext testCase = new UserContext(testChatId, testUserId, testName, "/help");
        String referent = """
            Available commands:
            /list - list of trackable links;
            /start - registration in service;
            /track - add new link/links in tracklist;
            /untrack - remove link from tracklist.""";
        SendMessage testResult = helpCommand.handle(testCase);
        assertEquals(referent, testResult.getParameters().get("text"));
    }
}
