package edu.java.scrapper.api;

import edu.java.DTO.requests.AddLinkRequest;
import edu.java.DTO.requests.RemoveLinkRequest;
import edu.java.DTO.resonses.LinkResponse;
import edu.java.DTO.resonses.ListLinksResponse;
import edu.java.scrapper.domain.DTO.Link;
import java.net.URI;
import edu.java.scrapper.services.LinkService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class LinksApiController {
    private final LinkService linkService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id")
        long userId,
        @RequestBody
        AddLinkRequest link
    ) {
        Link lnk = linkService.add(userId, link.link());
        return new ResponseEntity<>(new LinkResponse(lnk.ID(), URI.create(lnk.url())), HttpStatus.OK);
    }

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id")
        long userId,
        @RequestBody
        RemoveLinkRequest link
    ) {
        Link lnk = linkService.remove(userId, link.link());
        return new ResponseEntity<>(new LinkResponse(lnk.ID(), URI.create(lnk.url())), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long userId) {
        return new ResponseEntity<>(
            new ListLinksResponse(
                linkService.listAll(userId)
                .stream()
                .map(link -> new LinkResponse(link.ID(), URI.create(link.url())))
                .toArray(LinkResponse[]::new)
            ),
            HttpStatus.OK
        );
    }
}
