package edu.java.scrapper.api;

import edu.java.DTO.requests.AddLinkRequest;
import edu.java.DTO.requests.RemoveLinkRequest;
import edu.java.DTO.resonses.LinkResponse;
import edu.java.DTO.resonses.ListLinksResponse;
import edu.java.scrapper.model.DatabaseConnectorService;
import java.net.URI;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LinksApiController implements LinksApi {
    private final DatabaseConnectorService connectorService;

    @Override
    public ResponseEntity<LinkResponse> addLink(long userId, AddLinkRequest link) {
        connectorService.addLinkToUser(userId, link.link());
        return new ResponseEntity<>(
            new LinkResponse(new Random().nextLong(), URI.create(link.link())), HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<LinkResponse> deleteLink(long userId, RemoveLinkRequest link) {
        connectorService.removeLinkFromUser(userId, link.link());
        return new ResponseEntity<>(
            new LinkResponse(new Random().nextLong(), URI.create(link.link())), HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinks(long userId) {
        return new ResponseEntity<>(connectorService.getListOfLinks(userId), HttpStatus.OK);
    }
}
