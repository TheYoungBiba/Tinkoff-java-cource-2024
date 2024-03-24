package edu.java.scrapper.services;

import edu.java.scrapper.domain.DTO.Link;
import edu.java.scrapper.domain.DTO.User;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    Link add(Long userId, String url);

    Link update(Long linkId, OffsetDateTime updatedAt);

    Link remove(Long userId, String url);

    Link remove(Long linkId);

    List<Link> listAll(Long userId);

    List<Link> listAll(int countOfDaysUnchecked);

    List<User> listAllUsers(Long linkId);
}
