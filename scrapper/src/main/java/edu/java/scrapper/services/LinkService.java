package edu.java.scrapper.services;

import edu.java.scrapper.domain.DTO.Link;
import java.util.List;

public interface LinkService {
    Link add(Long userId, String url);

    Link remove(Long userId, String url);

    List<Link> listAll(Long userId);
}
