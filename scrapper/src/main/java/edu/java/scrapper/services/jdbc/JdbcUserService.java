package edu.java.scrapper.services.jdbc;

import edu.java.DTO.exceptions.ResourceNotFoundException;
import edu.java.scrapper.domain.UserRepository;
import edu.java.scrapper.domain.UsersLinksRepository;
import edu.java.scrapper.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JdbcUserService implements UserService {
    private final UserRepository userRepository;
    private final UsersLinksRepository usersLinksRepository;

    @Override
    public void register(Long ID) {
        if (userRepository.find(ID).isEmpty()) {
            userRepository.add(ID);
            log.info("User" + ID + "registered");
        } else {
            log.info("User " + ID + "already exists");
        }
    }

    @Override
    @Transactional
    public void unregister(Long ID) {
        if (userRepository.find(ID).isPresent()) {
            userRepository.remove(ID);
            if (usersLinksRepository.findByUserId(ID).isEmpty()) {
                usersLinksRepository.removeUser(ID);
            }
            log.info("User" + ID + "deleted");
        } else {
            log.info("User " + ID + "does not exist");
            throw new ResourceNotFoundException("User " + ID + "does not exist");
        }
    }
}
