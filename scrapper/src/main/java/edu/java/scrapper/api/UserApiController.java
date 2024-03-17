package edu.java.scrapper.api;

import edu.java.scrapper.model.DatabaseConnectorServiceDepricated;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat/{id}")
@AllArgsConstructor
public class UserApiController {
    private final DatabaseConnectorServiceDepricated connectorService;

    @PostMapping
    public ResponseEntity<Void> registerUser(@PathVariable("id") long userId) {
        connectorService.registerUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long userId) {
        connectorService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
