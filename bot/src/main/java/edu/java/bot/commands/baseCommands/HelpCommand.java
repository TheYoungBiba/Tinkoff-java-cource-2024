package edu.java.bot.commands.baseCommands;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.core.UserContext;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {
    private List<Command> commands;

    @Autowired
    public HelpCommand(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public String commandName() {
        return "/help";
    }

    @Override
    public String description() {
        return "available commands";
    }

    @Override
    public SendMessage handle(UserContext context) {
        StringBuilder builder = new StringBuilder("Available commands:").append(System.lineSeparator());
        commands.forEach(command -> {
            builder.append(command.commandName())
                .append(" - ")
                .append(command.description())
                .append(";")
                .append(System.lineSeparator());
        });
        builder.delete(builder.length() - 2, builder.length()).append(".");
        return new SendMessage(context.chatId(), builder.toString());
    }
}
