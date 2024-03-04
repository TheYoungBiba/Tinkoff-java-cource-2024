package edu.java.bot.model.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.core.UserContext;

@FunctionalInterface
public interface CommandHandler {
    SendMessage handle(UserContext context);
}
