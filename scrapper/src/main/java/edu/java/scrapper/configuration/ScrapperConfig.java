package edu.java.scrapper.configuration;

import edu.java.scrapper.database.InMemoryDatabase;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EnvironmentConfig.class)
public class ScrapperConfig {
    @Bean
    EnvironmentConfig.Scheduler scheduler(EnvironmentConfig environmentConfig) {
        return environmentConfig.scheduler();
    }

    @Bean
    InMemoryDatabase databaseConnection() {
        return new InMemoryDatabase();
    }
}
