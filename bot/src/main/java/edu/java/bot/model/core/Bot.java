package edu.java.bot.model.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.model.commands.Command;
import edu.java.bot.model.processors.UserMessageProcessor;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Bot implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final UserMessageProcessor processor;

    public Bot(TelegramBot telegramBot, UserMessageProcessor processor) {
        this.telegramBot = telegramBot;
        this.processor = processor;
        telegramBot.execute(new SetMyCommands(
            processor.getCommands().stream().map(Command::toApiCommand).toArray(BotCommand[]::new)
        ));
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            Message message = update.message();
            if (message != null) {
                UserContext context = new UserContext(
                    message.chat().id(),
                    message.from().id(),
                    message.from().firstName(),
                    message.text()
                );
                SendMessage ans = processor.process(context);
                telegramBot.execute(ans);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        telegramBot.execute(request);
    }
}
