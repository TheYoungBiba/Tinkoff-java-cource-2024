package edu.java.scrapper.database;

import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.*;

@Component
public class InMemoryDatabase {
    private final Map<Long, List<URI>> data;

    public InMemoryDatabase() {
        data = Collections.synchronizedMap(new HashMap<>());
    }

    public void addUser(Long userId) {
        data.put(userId, new LinkedList<>());
    }

    public void addUser(Long userId, List<URI> tracklist) {
        data.put(userId, tracklist);
    }

    public boolean isUserExist(Long userId) {
        return data.containsKey(userId);
    }

    public void addLinkToUser(Long userId, URI uri) {
        List<URI> tracklist = data.get(userId);
        tracklist.add(uri);
        data.put(userId, tracklist);
    }

    public void addLinksToUser(Long userId, List<URI> listOfUri) {
        List<URI> tracklist = data.get(userId);
        tracklist.addAll(listOfUri);
        data.put(userId, tracklist);
    }

    public void removeLinkFromUser(Long userId, URI urlToRemove) {
        List<URI> tracklist = data.get(userId);
        tracklist.remove(urlToRemove);
        data.put(userId, tracklist);
    }

    public void removeLinksFromUser(Long userId, List<URI> listToRemove) {
        List<URI> tracklist = data.get(userId);
        tracklist.removeAll(listToRemove);
        data.put(userId, tracklist);
    }

    public List<URI> getUserTracklist(Long userId) {
        return data.get(userId);
    }

    public void removeUser(Long userId) {
        data.remove(userId);
    }

    public void dropDatabase() {
        data.clear();
    }
}
