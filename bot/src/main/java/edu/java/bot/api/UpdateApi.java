package edu.java.bot.api;

import edu.java.DTO.requests.LinkUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/updates")
public interface UpdateApi {
    @PostMapping(consumes = "application/json")
    ResponseEntity<Void> sendUpdate(@RequestBody LinkUpdate update);
}
