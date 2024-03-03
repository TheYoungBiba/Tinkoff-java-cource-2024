package edu.java.scrapper.api;

import edu.java.scrapper.api.exceptionHandler.ApiException;
import edu.java.scrapper.database.InMemoryDatabase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class UserApiController implements UserApi {
    private final ;

    @Override
    public ResponseEntity<Void> registerUser(long userId) {
        valid(userId);
        databaseConnection.addUser(userId);
        log.info("User " + userId + " has registered");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteUser(long userId) {
        valid(userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void valid(long userId) {
        if (userId < 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid Telegram user ID");
        }
    }
}
