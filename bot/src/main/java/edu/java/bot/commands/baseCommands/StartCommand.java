package edu.java.bot.commands.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.core.UserContext;
import edu.java.bot.database.InMemoryDatabase;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    String botName;
    private InMemoryDatabase databaseConnection;

    public StartCommand(InMemoryDatabase databaseConnection, String botName) {
        this.databaseConnection = databaseConnection;
        this.botName = botName;
    }

    @Override
    public String commandName() {
        return "/start";
    }

    @Override
    public String description() {
        return "registration in service";
    }

    @Override
    public SendMessage handle(UserContext context) {
        if (databaseConnection.isUserExist(context.userId())) {
            return new SendMessage(
                context.chatId(),
                context.userName() + ", you already registered in "
                    + botName + ". To see your tracklist send /list"
            );
        }
        databaseConnection.addUser(context.userId());
        return new SendMessage(
            context.chatId(),
            "Hi, " + context.userName()
                + "! You successfully registered in " + botName + ". To add links for tracking send /track"
        );
    }
}
