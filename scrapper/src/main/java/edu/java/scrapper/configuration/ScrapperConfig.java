package edu.java.scrapper.configuration;

import edu.java.scrapper.gitHubClient.GitHubRepoClient;
import edu.java.scrapper.stackOverflowClient.StackOverflowQuestionClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EnvironmentConfig.class)
public class ScrapperConfig {
    @Bean
    StackOverflowQuestionClient stackOverflowQuestionClient(EnvironmentConfig environmentConfig) {
        return new StackOverflowQuestionClient(environmentConfig.stackOverflowClientUrl());
    }

    @Bean
    GitHubRepoClient gitHubRepoClient(EnvironmentConfig environmentConfig) {
        return new GitHubRepoClient(environmentConfig.gitHubClientUrl());
    }

    @Bean
    EnvironmentConfig.Scheduler scheduler(EnvironmentConfig environmentConfig) {
        return environmentConfig.scheduler();
    }
}
