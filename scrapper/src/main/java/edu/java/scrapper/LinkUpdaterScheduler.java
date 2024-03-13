package edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableScheduling
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        log.info("Updated.");
    }
}
