package edu.java.scrapper.api;

import edu.java.DTO.requests.AddLinkRequest;
import edu.java.DTO.requests.RemoveLinkRequest;
import edu.java.DTO.resonses.LinkResponse;
import edu.java.DTO.resonses.ListLinksResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/links")
public interface LinksApi {
    @GetMapping(produces = "application/json")
    ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long userId);

    @PostMapping(produces = "application/json", consumes = "application/json")
    ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") long userId, @RequestBody AddLinkRequest link);

    @DeleteMapping(produces = "application/json", consumes = "application/json")
    ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id")
        long userId,
        @RequestBody
        RemoveLinkRequest link
    );
}
