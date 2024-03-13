package edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ScrapperConfig.class)
public class Config {
    @Bean
    ScrapperConfig.Scheduler scheduler(ScrapperConfig scrapperConfig) {
        return scrapperConfig.scheduler();
    }

    @Bean
    @Value("endpoints.bot")
    String tgBotUrl(String tgBotUrl) {
        return tgBotUrl;
    }
}
