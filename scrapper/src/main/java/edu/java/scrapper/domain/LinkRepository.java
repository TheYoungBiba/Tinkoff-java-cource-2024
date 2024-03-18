package edu.java.scrapper.domain;

import edu.java.scrapper.domain.DTO.Link;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LinkRepository {
    void add(String url);

    void update(Long ID, OffsetDateTime updatedAt);

    default void addAll(List<String> links) {
        links.forEach(this::add);
    }

    Optional<Link> find(Long ID);

    Optional<Link> findByUrl(String url);

    List<Link> findAll();

    default List<Link> findAll(List<Long> IDs) {
        List<Link> links = new LinkedList<>();
        IDs.forEach(id -> find(id).ifPresent(links::add));
        return links;
    }

    List<Link> findAll(int countOfDaysUnchecked);

    void remove(Long ID);

    default void removeLink(Link link) {
        remove(link.ID());
    }

    default void removeAll(List<Long> IDs) {
        IDs.forEach(this::remove);
    }

    default void removeAllLinks(List<Link> links) {
        removeAll(links.stream().map(Link::ID).toList());
    }

    void dropTable();
}
