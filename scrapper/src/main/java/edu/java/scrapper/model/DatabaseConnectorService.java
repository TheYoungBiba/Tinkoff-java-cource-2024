package edu.java.scrapper.model;

import edu.java.scrapper.api.exceptionHandler.ApiException;
import edu.java.scrapper.database.InMemoryDatabase;
import edu.java.scrapper.model.DTO.LinkResponse;
import edu.java.scrapper.model.DTO.ListLinksResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class DatabaseConnectorService {
    private final InMemoryDatabase databaseConnection;

    public void registerUser(long userId) {
        idValidator(userId);
        databaseConnection.addUser(userId);
        log.info("User " + userId + " has registered");
    }

    public void deleteUser(long userId) {
        idValidator(userId);
        idDatabaseValidator(userId);
        databaseConnection.removeUser(userId);
        log.info("User " + userId + " has removed");
    }

    public ListLinksResponse getListOfLinks(long userId) {
        idValidator(userId);
        idDatabaseValidator(userId);
        List<URI> tracklist = databaseConnection.getUserTracklist(userId);
        Random random = new Random();
        return new ListLinksResponse(
            tracklist.stream().map(uri -> new LinkResponse(random.nextLong(), uri)).toArray(LinkResponse[]::new)
        );
    }

    public void addLinkToUser(long userId, String link) {
        idValidator(userId);
        idDatabaseValidator(userId);
        linkValidator(link);
        databaseConnection.addLinkToUser(userId, URI.create(link));
        log.info(link + " added to " + userId + " user");
    }

    public void removeLinkFromUser(long userId, String link) {
        idValidator(userId);
        idDatabaseValidator(userId);
        linkValidator(link);
        existenceLinkValidator(userId, link);
        databaseConnection.removeLinkFromUser(userId, URI.create(link));
        log.info(link + " removed from " + userId + " user");
    }

    private void idValidator(long userId) {
        if (userId < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid Telegram user ID: " + userId);
        }
    }

    private void idDatabaseValidator(long userId) {
        if (!databaseConnection.isUserExist(userId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "User " + userId + " does not exists");
        }
    }

    private void linkValidator(String link) {
        try {
            new URI(link);
        } catch (URISyntaxException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid link: " + link);
        }
    }

    private void existenceLinkValidator(long userId, String link) {
        if(databaseConnection.getUserTracklist(userId).stream().map(URI::toString).noneMatch(s -> s.equals(link))) {
            throw new ApiException(HttpStatus.NOT_FOUND, link + " does not exists");
        }
    }
}
