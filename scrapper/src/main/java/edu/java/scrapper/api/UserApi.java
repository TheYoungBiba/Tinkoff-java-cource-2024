package edu.java.scrapper.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tg-chat/{id}")
public interface UserApi {
    @PostMapping
    ResponseEntity<Void> registerUser(@PathVariable("id") long userId);

    @DeleteMapping
    ResponseEntity<Void> deleteUser(@PathVariable("id") long userId);
}
