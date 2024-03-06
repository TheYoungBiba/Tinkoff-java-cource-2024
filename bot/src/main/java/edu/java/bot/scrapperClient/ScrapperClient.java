package edu.java.bot.scrapperClient;

import edu.java.DTO.exceptions.InvalidRequestException;
import edu.java.DTO.exceptions.ResourceNotFoundException;
import edu.java.DTO.requests.AddLinkRequest;
import edu.java.DTO.requests.RemoveLinkRequest;
import edu.java.DTO.resonses.ApiErrorResponse;
import edu.java.DTO.resonses.LinkResponse;
import edu.java.DTO.resonses.ListLinksResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ScrapperClient {
    private String scrapperDefaultUrl = "http://localhost:8080";
    private final WebClient client;
    private final String linksRequestHeader = "Tg-Chat-Id";
    private final String linksPath = "/links";

    public ScrapperClient() {
        client = WebClient.create(scrapperDefaultUrl);
    }

    @Autowired
    public ScrapperClient(String scrapperUrl) {
        client = WebClient.create(scrapperUrl);
    }

    public Optional<Exception> registerUser(long userId) {
        return processUser(HttpMethod.POST, userId);
    }

    public Optional<Exception> removeUser(long userId) {
        return processUser(HttpMethod.DELETE, userId);
    }

    public Optional<LinkResponse> addLink(long userId, String url) {
        return client.post()
            .uri(linksPath)
            .header(linksRequestHeader, String.valueOf(userId))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new AddLinkRequest(url))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
            )
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new ResourceNotFoundException(apiErrorResponse.exceptionMessage()))
            )
            .bodyToMono(LinkResponse.class)
            .blockOptional();
    }

    public Optional<LinkResponse> removeLink(long userId, String url) {
        return client.method(HttpMethod.DELETE)
            .uri(linksPath)
            .header(linksRequestHeader, String.valueOf(userId))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new RemoveLinkRequest(url))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
            )
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new ResourceNotFoundException(apiErrorResponse.exceptionMessage()))
            )
            .bodyToMono(LinkResponse.class)
            .blockOptional();
    }

    public Optional<ListLinksResponse> getTracklist(long userId) {
        return client.get()
            .uri(linksPath)
            .header(linksRequestHeader, String.valueOf(userId))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
            )
            .onStatus(
                HttpStatus.NOT_FOUND::equals,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .map(apiErrorResponse -> new ResourceNotFoundException(apiErrorResponse.exceptionMessage()))
            )
            .bodyToMono(ListLinksResponse.class)
            .blockOptional();
    }

    private Optional<Exception> processUser(HttpMethod method, long userId) {
        try {
            client.method(method)
                .uri("/tg-chat/{id}", userId)
                .retrieve()
                .onStatus(
                    HttpStatus.BAD_REQUEST::equals,
                    response -> response.bodyToMono(ApiErrorResponse.class)
                        .map(apiErrorResponse -> new InvalidRequestException(apiErrorResponse.exceptionMessage()))
                )
                .onStatus(
                    HttpStatus.NOT_FOUND::equals,
                    response -> response.bodyToMono(ApiErrorResponse.class)
                        .map(apiErrorResponse -> new ResourceNotFoundException(apiErrorResponse.exceptionMessage()))
                )
                .bodyToMono(Void.class)
                .block();
        } catch (Exception e) {
            return Optional.of(e);
        }
        return Optional.empty();
    }
}
