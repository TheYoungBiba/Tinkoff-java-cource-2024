package edu.java.bot.model.processors;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.commands.Command;
import edu.java.bot.model.core.UserContext;
import java.util.List;

public interface UserMessageProcessor {
    List<Command> getCommands();

    void addCommand(Command command);

    void removeCommand(String commandName);

    SendMessage process(UserContext context);
}
