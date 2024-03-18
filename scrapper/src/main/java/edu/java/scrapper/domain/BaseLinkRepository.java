package edu.java.scrapper.domain;

import edu.java.scrapper.domain.DTO.Link;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BaseLinkRepository implements LinkRepository {
    private final JdbcClient jdbcClient;

    @Override
    public void add(String url) {
        jdbcClient.sql("INSERT INTO links VALUES (DEFAULT, ?, NULL, NOW())")
            .param(url)
            .update();
    }

    public void update(Long linkId, OffsetDateTime updatedAt) {
        jdbcClient.sql("UPDATE links SET updated_at = ?, checked_at = NOW() WHERE id = ?")
            .param(updatedAt)
            .param(linkId)
            .update();
    }

    @Override
    public Optional<Link> find(Long ID) {
        return jdbcClient.sql("SELECT * FROM links WHERE id = ?").param(ID).query(Link.class).optional();
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return jdbcClient.sql("SELECT * FROM links WHERE url = ?").param(url).query(Link.class).optional();
    }

    @Override
    public List<Link> findAll() {
        return jdbcClient.sql("SELECT * FROM links").query(Link.class).list();
    }

    @Override
    public List<Link> findAll(int countOfDaysUnchecked) {
        return jdbcClient.sql("SELECT * FROM links WHERE checked_at < (NOW() - INTERVAL ?)")
            .param(countOfDaysUnchecked + " days")
            .query(Link.class)
            .list();
    }

    @Override
    public void remove(Long ID) {
        jdbcClient.sql("DELETE FROM links WHERE id = ?").param(ID).update();
    }

    @Override
    public void dropTable() {
        jdbcClient.sql("DELETE FROM links").update();
        log.info("table links dropped");
    }
}
