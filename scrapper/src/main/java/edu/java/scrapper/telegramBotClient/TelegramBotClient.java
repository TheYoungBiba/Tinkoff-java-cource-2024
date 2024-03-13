package edu.java.scrapper.telegramBotClient;

import edu.java.DTO.exceptions.InvalidRequestException;
import edu.java.DTO.requests.LinkUpdate;
import edu.java.DTO.resonses.ApiErrorResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class TelegramBotClient {
    private String telegramBotDefaultUrl = "http://localhost:8090";
    private final WebClient client;

    public TelegramBotClient() {
        client = WebClient.create(telegramBotDefaultUrl);
    }

    @Autowired
    public TelegramBotClient(String tgBotUrl) {
        client = WebClient.create(tgBotUrl);
    }

    public Optional<Exception> sendUpdate(LinkUpdate update) {
        try {
            client.post()
                .uri("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(update)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(ApiErrorResponse.class)
                        .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
                )
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e) {
            return Optional.of(e);
        }
        return Optional.empty();
    }
}
