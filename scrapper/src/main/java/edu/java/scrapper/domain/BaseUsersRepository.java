package edu.java.scrapper.domain;

import edu.java.scrapper.domain.DTO.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BaseUsersRepository implements UserRepository {
    private final JdbcClient jdbcClient;

    public void add(Long ID) {
        jdbcClient.sql("INSERT INTO users VALUES (?)")
            .param(ID)
            .update();
        log.info(ID + " added to database");
    }

    public List<User> findAll() {
        return jdbcClient.sql("SELECT * FROM users").query(User.class).list();
    }

    public Optional<User> find(Long ID) {
        return jdbcClient.sql("SELECT * FROM users WHERE id = ?").param(ID).query(User.class).optional();
    }

    public void remove(Long ID) {
        jdbcClient.sql("DELETE FROM users WHERE id = ?").param(ID).update();
        log.info("user with id: " + ID + "was deleted from database");
    }

    public void dropTable() {
        jdbcClient.sql("DELETE FROM users").update();
        log.info("table users dropped");
    }
}
