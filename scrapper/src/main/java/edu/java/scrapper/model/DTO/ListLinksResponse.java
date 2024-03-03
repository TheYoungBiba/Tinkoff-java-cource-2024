package edu.java.scrapper.model.DTO;

public record ListLinksResponse(LinkResponse[] links, int size) {
    public ListLinksResponse(LinkResponse[] links) {
        this(links, links.length);
    }
}
