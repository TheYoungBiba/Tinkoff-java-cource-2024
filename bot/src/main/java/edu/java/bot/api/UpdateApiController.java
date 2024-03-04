package edu.java.bot.api;

import edu.java.DTO.requests.LinkUpdate;
import edu.java.bot.model.LinkUpdatesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UpdateApiController implements UpdateApi {
    private final LinkUpdatesService linkUpdatesService;

    @Override
    public ResponseEntity<Void> sendUpdate(LinkUpdate update) {
        linkUpdatesService.sendUpdates(update);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
