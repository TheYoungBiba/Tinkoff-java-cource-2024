package edu.java.bot.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.commands.baseCommands.TrackCommand;
import edu.java.bot.model.core.UserContext;
import edu.java.bot.database.InMemoryDatabase;
import edu.java.bot.model.validators.Validator;
import edu.java.bot.model.validators.baseValidators.GitHubValidator;
import edu.java.bot.model.validators.baseValidators.StackOverflowValidator;
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

public class TrackCommandTest {
    private final long testUserId = 123;
    private final long testChatId = testUserId;
    private final String testName = "test name";
    private final InMemoryDatabase testDatabaseConnection = new InMemoryDatabase();
    List<Validator> testValidators = List.of(new GitHubValidator(), new StackOverflowValidator());
    private final String testBotName = "test_name_bot";
    private final TrackCommand trackCommand = new TrackCommand(testDatabaseConnection, testValidators, testBotName);

    @AfterEach
    public void resetDatabase() {
        testDatabaseConnection.dropDatabase();
    }

    @Test
    public void emptyWithoutRegistrationHandleTest() {
        UserContext testCase = getTestContext("/track");
        SendMessage testResult = trackCommand.handle(testCase);
        String referent = """
            Congratulations, test name! You have registered in test_name_bot.
            Please send url/links in a message starting with /track
            Example:
            /track
            https://example.com/myLink/
            https://anotherExample.dev/exampleLink""";
        assertEquals(referent, testResult.getParameters().get("text"));
    }

    private static Stream<Arguments> provideArgumentsForWithRegistrationHandleTests() {
        return Stream.of(
            Arguments.of(
                "/track",
                """
                Please send url/links in a message starting with /track
                Example:
                /track
                https://example.com/myLink/
                https://anotherExample.dev/exampleLink""",
                Collections.EMPTY_LIST
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link""",
                "All links were successfully added in tracklist. To see your tracklist send /list",
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                )
            ),
            Arguments.of(
    "/track https://github.com/sanyarnd/tinkoff-java-course-2023/",
                "Link was successfully added in tracklist. To see your tracklist send /list",
                List.of(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"))
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                https://stackoverflow.com/search?q=unsupported%20link
                https://unsupportedlink.ru/test
                invalid/url""",
                """
                Links are added in tracklist:
                · https://github.com/sanyarnd/tinkoff-java-course-2023/
                · https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
                · https://stackoverflow.com/search?q=unsupported%20link
                To see all your tracklist send /list
                Invalid or unsupported input:
                · https://unsupportedlink.ru/test
                · invalid/url""",
                List.of(
                    URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
                    URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
                    URI.create("https://stackoverflow.com/search?q=unsupported%20link")
                )
            ),
            Arguments.of(
                """
                /track https://github.com/sanyarnd/tinkoff-java-course-2023/
                https://unsupportedlink.ru/test
                invalid/url""",
                """
                Link is added in tracklist:
                · https://github.com/sanyarnd/tinkoff-java-course-2023/
                To see all your tracklist send /list
                Invalid or unsupported input:
                · https://unsupportedlink.ru/test
                · invalid/url""",
                List.of(URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"))
            ),
            Arguments.of(
                """
                /track
                https://unsupportedlink.ru/test
                invalid/url""",
                """
                Bot supports the following resources:
                github.com
                stackoverflow.com
                """,
                Collections.EMPTY_LIST
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForWithRegistrationHandleTests")
    public void withRegistrationMessageHandleTest(String testCase, String outputReferent, List<URI> databaseReferent) {
        UserContext testContext = getTestContext(testCase);
        testDatabaseConnection.addUser(testContext.userId());
        SendMessage testResult = trackCommand.handle(testContext);
        assertEquals(outputReferent, testResult.getParameters().get("text"));
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForWithRegistrationHandleTests")
    public void withRegistrationDatabaseHandleTest(String testCase, String outputReferent, List<URI> databaseReferent) {
        UserContext testContext = getTestContext(testCase);
        testDatabaseConnection.addUser(testContext.userId());
        trackCommand.handle(testContext);
        assertEquals(databaseReferent, testDatabaseConnection.getUserTracklist(testContext.userId()));
    }

    @Test
    public void repeatativeInputHandleTest() {
        UserContext testCase = getTestContext(
            """
            /track
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link
            https://github.com/sanyarnd/tinkoff-java-course-2023/
            https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            https://stackoverflow.com/search?q=unsupported%20link"""
        );
        testDatabaseConnection.addUser(testCase.userId());
        SendMessage testResult = trackCommand.handle(testCase);
        String outputReferent = """
            Links are added in tracklist:
            · https://github.com/sanyarnd/tinkoff-java-course-2023/
            · https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c
            · https://stackoverflow.com/search?q=unsupported%20link
            To see all your tracklist send /list
            """;
        List<URI> databaseReferent = List.of(
            URI.create("https://github.com/sanyarnd/tinkoff-java-course-2023/"),
            URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c"),
            URI.create("https://stackoverflow.com/search?q=unsupported%20link")
        );
        assertEquals(databaseReferent, testDatabaseConnection.getUserTracklist(testCase.userId()));
        assertEquals(outputReferent, testResult.getParameters().get("text"));
    }

    private UserContext getTestContext(String testCase) {
        return new UserContext(testChatId, testUserId, testName, testCase);
    }
}
