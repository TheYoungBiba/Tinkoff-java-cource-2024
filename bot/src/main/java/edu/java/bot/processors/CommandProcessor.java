package edu.java.bot.processors;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import edu.java.bot.core.UserContext;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandProcessor implements UserMessageProcessor {
    @Getter
    private List<Command> commands;

    public CommandProcessor(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    public void addCommand(Command command) {
        commands.add(command);
    }

    @Override
    public void removeCommand(String commandName) {
        commands = commands.stream().filter(command -> !command.commandName().equals(commandName)).toList();
    }

    @Override
    public SendMessage process(UserContext context) {
        for (Command command : commands) {
            if (context.message().startsWith(command.commandName())) {
                return command.handle(context);
            }
        }
        return new SendMessage(context.chatId(), "No such available command. Use /help to see list of abilities");
    }
}
