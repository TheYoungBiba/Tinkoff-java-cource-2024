package edu.java.scrapper.api;

import edu.java.scrapper.model.DTO.AddLinkRequest;
import edu.java.scrapper.model.DTO.LinkResponse;
import edu.java.scrapper.model.DTO.ListLinksResponse;
import edu.java.scrapper.model.DTO.RemoveLinkRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/links")
public interface LinksApi {
    @GetMapping
    ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long userId);

    @PostMapping
    ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") long userId, @RequestBody AddLinkRequest link);

    @DeleteMapping
    ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id")
        long userId,
        @RequestBody
        RemoveLinkRequest link
    );
}
