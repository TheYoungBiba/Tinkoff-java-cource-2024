package edu.java.scrapper.api;

import edu.java.scrapper.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat/{id}")
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> registerUser(@PathVariable("id") long userId) {
        userService.register(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long userId) {
        userService.unregister(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
