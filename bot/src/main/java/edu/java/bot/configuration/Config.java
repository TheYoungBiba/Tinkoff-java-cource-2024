package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.validators.Validator;
import edu.java.bot.validators.baseValidators.GitHubValidator;
import edu.java.bot.validators.baseValidators.StackOverflowValidator;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BotConfig.class)
public class Config {
    @Bean
    TelegramBot telegramBot(BotConfig config) {
        return new TelegramBot(config.telegramToken());
    }

    @Bean
    String botName(BotConfig config) {
        return config.telegramName();
    }

    @Bean
    GitHubValidator gitHubValidator() {
        return new GitHubValidator();
    }

    @Bean
    StackOverflowValidator stackOverflowValidator() {
        return new StackOverflowValidator();
    }

    @Bean
    List<Validator> validators(
        GitHubValidator gitHubValidator,
        StackOverflowValidator stackOverflowValidator
    ) {
        return List.of(gitHubValidator, stackOverflowValidator);
    }

    @Bean
    @Value(value = "endpoints.scrapper")
    String scrapperUrl(String url) {
        return url;
    }
}
