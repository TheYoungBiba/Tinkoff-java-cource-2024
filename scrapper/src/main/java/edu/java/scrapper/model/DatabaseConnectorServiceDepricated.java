package edu.java.scrapper.model;

import edu.java.DTO.exceptions.InvalidRequestException;
import edu.java.DTO.exceptions.ResourceNotFoundException;
import edu.java.DTO.resonses.LinkResponse;
import edu.java.DTO.resonses.ListLinksResponse;
import edu.java.scrapper.database.InMemoryDatabase;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
@SuppressWarnings("MultipleStringLiterals")
public class DatabaseConnectorServiceDepricated {
    private final InMemoryDatabase databaseConnection;

    public void registerUser(long userId) {
        idValidator(userId);
        existenceUserValidator(userId);
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
        nonExistenceLinkValidator(userId, link);
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
            throw new InvalidRequestException("Invalid Telegram user ID: " + userId);
        }
    }

    private void existenceUserValidator(long userId) {
        if (databaseConnection.isUserExist(userId)) {
            throw new InvalidRequestException("User " + userId + " already registered");
        }
    }

    private void idDatabaseValidator(long userId) {
        if (!databaseConnection.isUserExist(userId)) {
            throw new ResourceNotFoundException("User " + userId + " does not exists");
        }
    }

    private void linkValidator(String link) {
        try {
            new URI(link);
        } catch (URISyntaxException e) {
            throw new InvalidRequestException("Invalid link: " + link);
        }
    }

    private void nonExistenceLinkValidator(long userId, String link) {
        if (databaseConnection.getUserTracklist(userId).stream().map(URI::toString).anyMatch(s -> s.equals(link))) {
            throw new InvalidRequestException(link + " already exits");
        }
    }

    private void existenceLinkValidator(long userId, String link) {
        if (databaseConnection.getUserTracklist(userId).stream().map(URI::toString).noneMatch(s -> s.equals(link))) {
            throw new ResourceNotFoundException(link + " does not exists");
        }
    }
}
