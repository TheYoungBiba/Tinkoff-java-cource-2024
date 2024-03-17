package edu.java.scrapper.domain;

import edu.java.scrapper.domain.DTO.UserLinkRelation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BaseUsersLinksRepository implements UsersLinksRepository {
    private final JdbcClient jdbcClient;

    public void add(UserLinkRelation relation) {
        jdbcClient.sql("INSERT INTO users_links VALUES (?, ?)")
            .param(relation.userId())
            .param(relation.linkId())
            .update();
        log.info(relation + "added in database");
    }

    @Override
    public Optional<UserLinkRelation> find(UserLinkRelation relation) {
        return jdbcClient.sql("SELECT * FROM users_links WHERE user_id = ? AND link_id = ?")
            .param(relation.userId())
            .param(relation.linkId())
            .query(UserLinkRelation.class)
            .optional();
    }

    @Override
    public List<Long> findByUserId(Long userId) {
        return jdbcClient.sql("SELECT link_id FROM users_links WHERE user_id = ?")
            .param(userId)
            .query(Long.class)
            .list();
    }

    @Override
    public List<Long> findByLinkId(Long linkId) {
        return jdbcClient.sql("SELECT user_id FROM users_links WHERE link_id = ?")
            .param(linkId)
            .query(Long.class)
            .list();
    }

    @Override
    public List<UserLinkRelation> findAll() {
        return jdbcClient.sql("SELECT * FROM users_links")
            .query(UserLinkRelation.class)
            .list();
    }

    @Override
    public void remove(UserLinkRelation relation) {
        jdbcClient.sql("DELETE FROM users_links WHERE user_id = ? AND link_id = ?")
            .param(relation.userId())
            .param(relation.linkId())
            .update();
        log.info(relation + "was removed from database");
    }

    @Override
    public void removeUser(Long userId) {
        jdbcClient.sql("DELETE FROM users_links WHERE user_id = ?")
            .param(userId)
            .update();
        log.info("user: " + userId + "was removed from database");
    }

    @Override
    public void dropTable() {
        jdbcClient.sql("DELETE FROM users_links").update();
        log.info("table users dropped");
    }
}
