package edu.java.scrapper.api;

import edu.java.scrapper.model.DatabaseConnectorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserApiController implements UserApi {
    private final DatabaseConnectorService connectorService;

    @Override
    public ResponseEntity<Void> registerUser(long userId) {
        connectorService.registerUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(long userId) {
        connectorService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
