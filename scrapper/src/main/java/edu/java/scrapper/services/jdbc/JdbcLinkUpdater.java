package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.UserRepository;
import edu.java.scrapper.services.LinkUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    @Override
    public void update() {

    }
}
