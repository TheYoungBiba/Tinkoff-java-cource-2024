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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@AllArgsConstructor
public class LinksApiController {
    private final DatabaseConnectorService connectorService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id")
        long userId,
        @RequestBody
        AddLinkRequest link
    ) {
        connectorService.addLinkToUser(userId, link.link());
        return new ResponseEntity<>(
            new LinkResponse(new Random().nextLong(), URI.create(link.link())), HttpStatus.OK
        );
    }

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id")
        long userId,
        @RequestBody
        RemoveLinkRequest link
    ) {
        connectorService.removeLinkFromUser(userId, link.link());
        return new ResponseEntity<>(
            new LinkResponse(new Random().nextLong(), URI.create(link.link())), HttpStatus.OK
        );
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long userId) {
        return new ResponseEntity<>(connectorService.getListOfLinks(userId), HttpStatus.OK);
    }
}
