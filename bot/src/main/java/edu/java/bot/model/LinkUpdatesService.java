package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.DTO.exceptions.InvalidRequestException;
import edu.java.DTO.requests.LinkUpdate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkUpdatesService {
    private final TelegramBot bot;

    public void sendUpdates(LinkUpdate update) {
        linkValidator(update.url());
        userIdsValidator(update.tgChatIds());
        String message = getMessage(update);
        for (long id : update.tgChatIds()) {
            bot.execute(new SendMessage(id, message));
        }
    }

    private String getMessage(LinkUpdate update) {
        return "Detected changes on"
            + System.lineSeparator()
            + update.url()
            + System.lineSeparator()
            + update.description();
    }

    private void linkValidator(String link) {
        try {
            new URI(link);
        } catch (URISyntaxException e) {
            throw new InvalidRequestException("Invalid link: " + link);
        }
    }

    private void userIdsValidator(long[] userIds) {
        if (Arrays.stream(userIds).anyMatch(userId -> userId < 0)) {
            throw new InvalidRequestException("Invalid userIds");
        }
    }
}
