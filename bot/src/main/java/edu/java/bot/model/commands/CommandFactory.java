package edu.java.bot.model.commands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.core.UserContext;

public class CommandFactory {
    private CommandFactory() {}

    public static Command create(String command, String description, CommandHandler handler) {
        return new Command() {
            @Override
            public String commandName() {
                return command;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public SendMessage handle(UserContext context) {
                return handler.handle(context);
            }
        };
    }

    public static Command create(String command, CommandHandler handler) {
        return new Command() {
            @Override
            public String commandName() {
                return command;
            }

            @Override
            public String description() {
                return "";
            }

            @Override
            public SendMessage handle(UserContext context) {
                return handler.handle(context);
            }
        };
    }
}
