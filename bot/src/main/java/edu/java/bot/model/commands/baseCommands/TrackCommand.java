package edu.java.bot.model.commands.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.database.InMemoryDatabase;
import edu.java.bot.model.commands.Command;
import edu.java.bot.model.core.UserContext;
import edu.java.bot.model.validators.Validator;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
    private InMemoryDatabase databaseConnection;
    @Getter
    private List<Validator> validators;
    private String botName;
    private final String requestMessage = """
            Please send url/links in a message starting with /track
            Example:
            /track
            https://example.com/myLink/
            https://anotherExample.dev/exampleLink""";
    private final String successMessageLots =
        "All links were successfully added in tracklist. To see your tracklist send /list";
    private final String successMessageSingle =
        "Link was successfully added in tracklist. To see your tracklist send /list";

    public TrackCommand(InMemoryDatabase databaseConnection, List<Validator> validators, String botName) {
        this.databaseConnection = databaseConnection;
        this.validators = validators;
        this.botName = botName;
    }

    @Override
    public String commandName() {
        return "/track";
    }

    @Override
    public String description() {
        return "add new url/links in tracklist";
    }

    public void addNewValidator(Validator validator) {
        validators.add(validator);
    }

    @Override
    public SendMessage handle(UserContext context) {
        if (databaseConnection.isUserExist(context.userId())) {
            String[] split = context.message().split("(\\n|\\s)+");
            if (split.length == 1) {
                return new SendMessage(context.chatId(), requestMessage);
            }
            String response;
            List<URI> tracklist = getTracklist(split, databaseConnection.getUserTracklist(context.userId()));
            databaseConnection.addLinksToUser(context.userId(), tracklist);
            if (tracklist.isEmpty()) {
                response = getSupportedLinks();
            } else if (tracklist.size() < split.length - 1) {
                response = getCombinedResponse(split, tracklist);
            } else {
                response = split.length == 2 ? successMessageSingle : successMessageLots;
            }
            return new SendMessage(context.chatId(), response);
        }
        databaseConnection.addUser(context.userId());
        return new SendMessage(context.chatId(),
                "Congratulations, "
                    + context.userName()
                    + "! You have registered in " + botName + "."
                    + System.lineSeparator()
                    + handle(context).getParameters().get("text")
            );
    }

    private List<URI> getTracklist(String[] split, List<URI> previousTracklist) {
        Set<URI> tracklist = new HashSet<>();
        for (int i = 1; i < split.length; i++) {
            try {
                URI uri = new URI(split[i]);
                if (validators.stream().anyMatch(validator -> validator.isValid(uri))
                    && !previousTracklist.contains(uri)) {
                    tracklist.add(uri);
                }
            } catch (URISyntaxException ignored) { }
        }
        return tracklist.stream().sorted().toList();
    }

    @SuppressWarnings("LineLength")
    private String getSupportedLinks() {
        return validators.stream().reduce(
            new StringBuilder(
                validators.size() == 1 ? "Bot supports the following resource:" : "Bot supports the following resources:"
            ).append(System.lineSeparator()),
            (builder, validator) -> builder.append(validator.name()).append(System.lineSeparator()),
            StringBuilder::append
        ).toString();
    }

    private String getCombinedResponse(String[] split, List<URI> tracklist) {
        return tracklist.stream().reduce(
            new StringBuilder(
                tracklist.size() == 1 ? "Link is added in tracklist:" : "Links are added in tracklist:"
            ).append(System.lineSeparator()),
                (stringBuilder, uri) -> stringBuilder.append("· ").append(uri).append(System.lineSeparator()),
                StringBuilder::append
            ).append("To see all your tracklist send /list")
            .append(System.lineSeparator())
            .append(getInvalidLinks(split))
            .toString();
    }

    private String getInvalidLinks(String[] split) {
        String invalidInputHeader = "Invalid or unsupported input:\n";
        StringBuilder builder = new StringBuilder(invalidInputHeader);
        for (int i = 1; i < split.length; i++) {
            try {
                URI uri = new URI(split[i]);
                if (validators.stream().noneMatch(validator -> validator.isValid(uri))) {
                    builder.append("· ").append(split[i]).append(System.lineSeparator());
                }
            } catch (URISyntaxException invalid) {
                builder.append("· ").append(split[i]).append(System.lineSeparator());
            }
        }
        return builder.length() == invalidInputHeader.length() ? "" : builder.deleteCharAt(builder.length() - 1)
            .toString();
    }
}
