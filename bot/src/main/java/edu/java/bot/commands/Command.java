package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.core.UserContext;

public interface Command {
    String commandName();

    String description();

    SendMessage handle(UserContext context);

    default BotCommand toApiCommand() {
        return new BotCommand(commandName(), description());
    }
}
