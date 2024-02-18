package edu.java.bot.commands.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.core.UserContext;
import edu.java.bot.database.InMemoryDatabase;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {
    private InMemoryDatabase databaseConnection;

    @Autowired
    public ListCommand(InMemoryDatabase databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public String commandName() {
        return "/list";
    }

    @Override
    public String description() {
        return "list of trackable links";
    }

    @Override
    public SendMessage handle(UserContext context) {
        if (databaseConnection.isUserExist(context.userId())) {
            List<URI> tracklist = databaseConnection.getUserTracklist(context.userId());
            return new SendMessage(
                context.chatId(),
                context.userName() + "'s tracklist:" + System.lineSeparator()
                    + tracklist.stream().reduce(
                    new StringBuilder(),
                    (stringBuilder, uri) -> stringBuilder.append("· ").append(uri).append(System.lineSeparator()),
                    StringBuilder::append
                )
            );
        }
        databaseConnection.addUser(context.userId());
        return new SendMessage(
            context.chatId(),
            "You successfully registered! To add links in your tracklist use /track"
        );
    }
}
