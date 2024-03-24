package edu.java.scrapper.services.jdbc;

import edu.java.DTO.exceptions.InvalidRequestException;
import edu.java.DTO.exceptions.ResourceNotFoundException;
import edu.java.scrapper.domain.DTO.Link;
import edu.java.scrapper.domain.DTO.User;
import edu.java.scrapper.domain.DTO.UserLinkRelation;
import edu.java.scrapper.domain.LinkRepository;
import edu.java.scrapper.domain.UserRepository;
import edu.java.scrapper.domain.UsersLinksRepository;
import edu.java.scrapper.services.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final UsersLinksRepository usersLinksRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Link add(Long userId, String url) {
        userCheck(userId);
        Optional<Link> linkOptional = linkRepository.findByUrl(url);
        if (linkOptional.isPresent()) {
            UserLinkRelation relation = new UserLinkRelation(userId, linkOptional.get().ID());
            if (usersLinksRepository.find(relation).isPresent()) {
                log.info("User " + userId + " already has link: " + url + " in tracklist");
                throw new InvalidRequestException("User " + userId + " already has link: " + url + " in tracklist");
            }
            usersLinksRepository.add(relation);
            return linkOptional.get();
        } else {
            linkRepository.add(url);
            Link link = linkRepository.findByUrl(url).get();
            usersLinksRepository.add(new UserLinkRelation(userId, link.ID()));
            return link;
        }
    }

    @Override
    @Transactional
    public Link update(Long linkId, OffsetDateTime updatedAt) {
        linkRepository.update(linkId, updatedAt);
        return linkRepository.find(linkId).get();
    }

    @Override
    @Transactional
    public Link remove(Long userId, String url) {
        userCheck(userId);
        Optional<Link> linkOptional = linkRepository.findByUrl(url);
        if (linkOptional.isEmpty()) {
            log.info("Link: " + url + " does not exist");
            throw new ResourceNotFoundException("Link: " + url + " does not exist");
        }
        Optional<UserLinkRelation> relationOptional =
            usersLinksRepository.find(new UserLinkRelation(userId, linkOptional.get().ID()));
        if (relationOptional.isEmpty()) {
            log.info("There is no in user " + userId + " such link: " + url);
            throw new InvalidRequestException("There is no in user " + userId + " such link: " + url);
        }
        usersLinksRepository.remove(relationOptional.get());
        if (usersLinksRepository.findByLinkId(linkOptional.get().ID()).isEmpty()) {
            linkRepository.remove(linkOptional.get().ID());
        }
        return linkOptional.get();
    }

    @Override
    @Transactional
    public Link remove(Long linkId) {
        List<Long> usersWithRemovableLink = usersLinksRepository.findByLinkId(linkId);
        usersLinksRepository.removeAll(
            usersWithRemovableLink.stream().map(userId -> new UserLinkRelation(userId, linkId)).toList()
        );
        Link link = linkRepository.find(linkId).get();
        linkRepository.remove(linkId);
        return link;
    }

    @Override
    public List<Link> listAll(Long userId) {
        userCheck(userId);
        List<Long> linksIds = usersLinksRepository.findByUserId(userId);
        return linkRepository.findAll(linksIds);
    }

    @Override
    public List<Link> listAll(int countOfDaysUnchecked) {
        return linkRepository.findAll(countOfDaysUnchecked);
    }

    @Override
    public List<User> listAllUsers(Long linkId) {
        return usersLinksRepository.findByLinkId(linkId).stream().map(User::new).toList();
    }

    private void userCheck(Long userId) {
        if (userRepository.find(userId).isEmpty()) {
            log.info("User " + userId + "does not exist");
            throw new ResourceNotFoundException("User " + userId + "does not exist");
        }
    }
}
