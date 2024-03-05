package edu.java.bot.commands.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.database.InMemoryDatabase;
import edu.java.bot.core.UserContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    private InMemoryDatabase databaseConnection;
    private String botName;
    private final String requestMessage = """
            Please send links in a message starting with /untrack
            Example:
            /untrack
            https://example.com/myLink/
            https://anotherExample.dev/exampleLink""";
    private final String successMessageLots =
        "All links were successfully removed from tracklist. To see your tracklist send /list";
    private final String successMessageSingle =
        "Link was successfully removed from tracklist. To see your tracklist send /list";

    public UntrackCommand(InMemoryDatabase databaseConnection, String botName) {
        this.databaseConnection = databaseConnection;
        this.botName = botName;
    }

    @Override
    public String commandName() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "remove link from tracklist";
    }

    @Override
    @SuppressWarnings("ReturnCount")
    public SendMessage handle(UserContext context) {
        if (databaseConnection.isUserExist(context.userId())) {
            String[] split = context.message().split("(\\n|\\s)+");
            if (split.length == 1) {
                return new SendMessage(context.chatId(), requestMessage);
            }
            List<URI> removeList = getLinksToRemove(split, databaseConnection.getUserTracklist(context.userId()));
            databaseConnection.removeLinksFromUser(context.userId(), removeList);
            if (removeList.isEmpty()) {
                return new SendMessage(
                    context.chatId(),
                    "Invalid or unsupported input, check your tracklist with /list"
                );
            }
            if (removeList.size() < split.length - 1) {
                return new SendMessage(context.chatId(), getCombinedResponse(split, removeList));
            }
            return new SendMessage(context.chatId(), split.length == 2 ? successMessageSingle : successMessageLots);
        }
        databaseConnection.addUser(context.userId());
        return new SendMessage(
            context.chatId(),
            "Congratulations, " + context.userName() + "! You have registered in " + botName + "."
        );
    }

    private List<URI> getLinksToRemove(String[] split, List<URI> tracklist) {
        List<URI> removeList = new LinkedList<>();
        for (int i = 1; i < split.length; i++) {
            try {
                URI uri = new URI(split[i]);
                if (tracklist.contains(uri)) {
                    removeList.add(uri);
                }
            } catch (URISyntaxException ignored) { }
        }
        return removeList;
    }

    private String getCombinedResponse(String[] split, List<URI> removeList) {
        return new StringBuilder(
            removeList.size() == 1 ? "Link is removed from tracklist:" : "Links are removed from tracklist:"
        ).append(System.lineSeparator())
            .append(removeList.stream().reduce(
                new StringBuilder(),
                (stringBuilder, uri) -> stringBuilder.append("· ").append(uri).append(System.lineSeparator()),
                StringBuilder::append
            ))
            .append("To see your tracklist send /list")
            .append(System.lineSeparator())
            .append(getInvalidLinks(split, removeList))
            .toString();
    }

    private String getInvalidLinks(String[] split, List<URI> removeList) {
        StringBuilder builder = new StringBuilder("Invalid or unsupported input:");
        builder.append(System.lineSeparator());
        for (int i = 1; i < split.length; i++) {
            try {
                URI uri = new URI(split[i]);
                if (!removeList.contains(uri)) {
                    builder.append("· ").append(split[i]).append(System.lineSeparator());
                }
            } catch (URISyntaxException invalid) {
                builder.append("· ").append(split[i]).append(System.lineSeparator());
            }
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }
}
