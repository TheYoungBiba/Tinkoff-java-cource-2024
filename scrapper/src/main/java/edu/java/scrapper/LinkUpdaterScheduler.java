package edu.java.scrapper;

import edu.java.scrapper.services.LinkUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@EnableScheduling
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@scheduler.interval()}")
    public void update() {
        linkUpdater.update();
        log.info("Updated.");
    }
}
