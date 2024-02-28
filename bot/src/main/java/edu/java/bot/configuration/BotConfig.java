package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.baseCommands.HelpCommand;
import edu.java.bot.commands.baseCommands.ListCommand;
import edu.java.bot.commands.baseCommands.StartCommand;
import edu.java.bot.commands.baseCommands.TrackCommand;
import edu.java.bot.commands.baseCommands.UntrackCommand;
import edu.java.bot.validators.Validator;
import edu.java.bot.validators.baseValidators.GitHubValidator;
import edu.java.bot.validators.baseValidators.StackOverflowValidator;
import java.util.List;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EnvironmentConfig.class)
public class BotConfig {
    @Bean
    TelegramBot telegramBot(EnvironmentConfig config) {
        return new TelegramBot(config.telegramToken());
    }

    @Bean
    String botName(EnvironmentConfig config) {
        return config.telegramName();
    }

    @Bean
    List<Command> commands(
        StartCommand startCommand,
        HelpCommand helpCommand,
        TrackCommand trackCommand,
        UntrackCommand untrackCommand,
        ListCommand listCommand
    ) {
        return List.of(startCommand, helpCommand, trackCommand, untrackCommand, listCommand);
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
}