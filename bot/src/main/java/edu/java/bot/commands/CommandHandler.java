package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.core.UserContext;

@FunctionalInterface
public interface CommandHandler {
    SendMessage handle(UserContext context);
}
