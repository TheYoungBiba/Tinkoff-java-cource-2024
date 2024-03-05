package edu.java.bot.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.baseCommands.TrackCommand;
import edu.java.bot.commands.baseCommands.UntrackCommand;
import edu.java.bot.core.UserContext;
import edu.java.bot.database.InMemoryDatabase;
import edu.java.bot.validators.Validator;
import edu.java.bot.validators.baseValidators.GitHubValidator;
import edu.java.bot.validators.baseValidators.StackOverflowValidator;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UntrackCommandTest {
    private final long testUserId = 123;
    private final long testChatId = testUserId;
    private final String testName = "test name";
    private final InMemoryDatabase testDatabaseConnection = new InMemoryDatabase();
    List<Validator> testValidators = List.of(new GitHubValidator(), new StackOverflowValidator());
    private final String testBotName = "test_name_bot";
    private final TrackCommand trackCommand = new TrackCommand(testDatabaseConnection, testValidators, testBotName);
    private final UntrackCommand untrackCommand = new UntrackCommand(testDatabaseConnection, testBotName);

    @AfterEach
    public void resetDatabase() {
        testDatabaseConnection.dropDatabase();
    }

    @Test
    public void emptyWithoutRegistrationHandleTest() {
        UserContext testCase = getTestContext("/untrack");
        SendMessage testResult = untrackCommand.handle(testCase);
        String referent = "Congratulations, test name! You have registered in test_name_bot.";
        assertEquals(referent, testResult.getParameters().get("text"));
    }

    private static Stream<Arguments> provideArgumentsForWithRegistrationHandleTests() {
        return Stream.of(
            Arguments.of(
                "/track",
                "/untrack",
                """
                Please send links in a message starting with /untrack
                Example:
                /untrack
                https://example.com/myLink/
                https://anotherExample.dev/exampleLink""",
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""",
                """
                /untrack https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""",
                "All links were successfully removed from tracklist. To see your tracklist send /list",
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                ),
                Collections.EMPTY_LIST
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""",
                "/untrack https://github.com/sanyarnd/tinkoff-java-course-2023/",
                "Link was successfully removed from tracklist. To see your tracklist send /list",
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                ),
                List.of(
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                )
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://unsupportedlink.ru/test
                invalid/url""",
                """
                /untrack https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link
                https://unsupportedlink.ru/test
                invalid/url""",
                """
                Links are removed from tracklist:
                · https://github.com/sanyarnd/tinkoff-java-course-2023/
                · https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                To see your tracklist send /list
                Invalid or unsupported input:
                · https://stackoverflow.com/search?q=unsupported%20link
                · https://unsupportedlink.ru/test
                · invalid/url""",
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c")
                ),
                Collections.EMPTY_LIST
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://unsupportedlink.ru/test
                invalid/url""",
                """
                /untrack https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://unsupportedlink.ru/test
                invalid/url""",
                """
                Link is removed from tracklist:
                · https://github.com/sanyarnd/tinkoff-java-course-2023/
                To see your tracklist send /list
                Invalid or unsupported input:
                · https://unsupportedlink.ru/test
                · invalid/url""",
                List.of(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/")),
                Collections.EMPTY_LIST
            ),
            Arguments.of(
                """
                /track
                https://unsupportedlink.ru/test
                invalid/url""",
                """
                /untrack
                https://unsupportedlink.ru/test
                invalid/url""",
                "Invalid or unsupported input, check your tracklist with /list",
                Collections.EMPTY_LIST,
                Collections.EMPTY_LIST
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForWithRegistrationHandleTests")
    public void withRegistrationMessageHandleTest(
        String testInput,
        String testCase,
        String outputReferent,
        List<URI> intermediateDatabaseCondition,
        List<URI> databaseReferent
    ) {
        UserContext testContext = getTestContext(testCase);
        testDatabaseConnection.addUser(testContext.userId());
        trackCommand.handle(getTestContext(testInput));
        SendMessage testResult = untrackCommand.handle(testContext);
        assertEquals(outputReferent, testResult.getParameters().get("text"));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForWithRegistrationHandleTests")
    public void withRegistrationDatabaseHandleTest(
        String testInput,
        String testCase,
        String outputReferent,
        List<URI> intermediateDatabaseCondition,
        List<URI> databaseReferent
    ) {
        UserContext testContext = getTestContext(testCase);
        testDatabaseConnection.addUser(testContext.userId());
        trackCommand.handle(getTestContext(testInput));
        assertEquals(intermediateDatabaseCondition, testDatabaseConnection.getUserTracklist(testContext.userId()));
        untrackCommand.handle(testContext);
        assertEquals(databaseReferent, testDatabaseConnection.getUserTracklist(testContext.userId()));
    }

    private UserContext getTestContext(String testCase) {
        return new UserContext(testChatId, testUserId, testName, testCase);
    }
}
