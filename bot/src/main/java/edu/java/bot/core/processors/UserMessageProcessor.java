package edu.java.bot.core.processors;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.core.UserContext;
import java.util.List;

public interface UserMessageProcessor {
    List<Command> getCommands();

    void addCommand(Command command);

    void removeCommand(String commandName);

    SendMessage process(UserContext context);
}
