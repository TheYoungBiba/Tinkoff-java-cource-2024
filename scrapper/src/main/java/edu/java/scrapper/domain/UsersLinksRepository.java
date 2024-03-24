package edu.java.scrapper.domain;

import edu.java.scrapper.domain.DTO.UserLinkRelation;
import java.util.List;
import java.util.Optional;

public interface UsersLinksRepository {
    void add(UserLinkRelation relation);

    default void addAll(List<UserLinkRelation> relations) {
        relations.forEach(this::add);
    }

    Optional<UserLinkRelation> find(UserLinkRelation relation);

    List<Long> findByUserId(Long userId);

    List<Long> findByLinkId(Long linkId);

    List<UserLinkRelation> findAll();

    void remove(UserLinkRelation relation);

    void removeUser(Long userId);

    default void removeAll(List<UserLinkRelation> relations) {
        relations.forEach(this::remove);
    }

    void dropTable();
}
