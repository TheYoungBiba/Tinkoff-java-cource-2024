package edu.java.DTO.resonses;

public record ListLinksResponse(LinkResponse[] links, int size) {
    public ListLinksResponse(LinkResponse[] links) {
        this(links, links.length);
    }
}
