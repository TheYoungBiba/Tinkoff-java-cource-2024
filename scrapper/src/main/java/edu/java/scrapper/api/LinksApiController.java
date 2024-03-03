package edu.java.scrapper.api;

import edu.java.scrapper.model.DTO.AddLinkRequest;
import edu.java.scrapper.model.DTO.LinkResponse;
import edu.java.scrapper.model.DTO.ListLinksResponse;
import edu.java.scrapper.model.DTO.RemoveLinkRequest;
import edu.java.scrapper.model.DatabaseConnectorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LinksApiController implements LinksApi {
    private final DatabaseConnectorService connectorService;

    @Override
    public ResponseEntity<LinkResponse> addLink(long userId, AddLinkRequest link) {
        return null;
    }

    @Override
    public ResponseEntity<LinkResponse> deleteLink(long userId, RemoveLinkRequest link) {
        return null;
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinks(long userId) {
        return null;
    }
}
