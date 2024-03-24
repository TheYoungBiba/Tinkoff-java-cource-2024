package edu.java.scrapper.services;

import edu.java.DTO.requests.LinkUpdate;
import edu.java.scrapper.domain.DTO.Link;
import edu.java.scrapper.domain.DTO.User;
import edu.java.scrapper.gitHubClient.GitHubRepo;
import edu.java.scrapper.gitHubClient.GitHubRepoClient;
import edu.java.scrapper.gitHubClient.GitHubRepoResponse;
import edu.java.scrapper.stackOverflowClient.StackOverflowQuestionClient;
import edu.java.scrapper.stackOverflowClient.StackOverflowQuestionResponse;
import edu.java.scrapper.telegramBotClient.TelegramBotClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaseLinkUpdater implements LinkUpdater {
    private final LinkService linkService;
    private final StackOverflowQuestionClient stackOverflowQuestionClient;
    private final GitHubRepoClient gitHubRepoClient;
    private final TelegramBotClient telegramBotClient;
    private final int countOdDaysThatLinkWasWithoutCheck = 0;

    @Override
    public void update() {
        linkService.listAll(countOdDaysThatLinkWasWithoutCheck).forEach(link -> {
            try {
                if (link.url().matches("^https://stackoverflow\\.com/questions/(\\d+)/(\\S+)$")) {
                    telegramBotClient.sendUpdate(stackOverflowUpdate(link));
                }
                if (link.url().matches("^https://github\\.com/(\\S+)/(\\S+)$")) {
                    telegramBotClient.sendUpdate(gitHubUpdate(link));
                }
                throw new RuntimeException("Link " + link.url() + " is invalid. It was removed from tracklist.");
            } catch (Exception e) {
                telegramBotClient.sendUpdate(new LinkUpdate(
                    link.ID(),
                    link.url(),
                    e.getMessage(),
                    linkService.listAllUsers(link.ID()).stream().map(User::ID).toArray(Long[]::new)
                ));
                linkService.remove(link.ID());
            }
        });
    }

    private LinkUpdate stackOverflowUpdate(Link stackOverflowLink) throws URISyntaxException {
        Optional<StackOverflowQuestionResponse> response =
            stackOverflowQuestionClient.fetchQuestion(parseQuestionId(stackOverflowLink.url()));
        Long[] usersOfLink =
            linkService.listAllUsers(stackOverflowLink.ID()).stream().map(User::ID).toArray(Long[]::new);
        if (response.isEmpty()) {
            LinkUpdate update =  new LinkUpdate(
                stackOverflowLink.ID(),
                stackOverflowLink.url(),
                "Link "
                    + stackOverflowLink.url()
                    + "already does not exists or invalid. It was removed from tracklist.",
                usersOfLink
            );
            linkService.remove(stackOverflowLink.ID());
            return update;
        }
        LinkUpdate update = new LinkUpdate(
            stackOverflowLink.ID(),
            stackOverflowLink.url(),
//            stackOverflowResponseProcessor.process(response.get());
            "Link " + stackOverflowLink.url() + " updated at :" + response.get().lastActivityDate(),
            usersOfLink
        );
        linkService.update(stackOverflowLink.ID(), response.get().lastActivityDate());
        return update;
    }

    private Long parseQuestionId(String link) throws URISyntaxException {
        URI url = new URI(link);
        String[] pathValues = url.getPath().split("/");
        return Long.parseLong(pathValues[2]);
    }

    private LinkUpdate gitHubUpdate(Link gitHubLink) throws URISyntaxException {
        Optional<GitHubRepoResponse> response = gitHubRepoClient.fetchRepository(parseRepo(gitHubLink.url()));
        Long[] usersOfLink =
            linkService.listAllUsers(gitHubLink.ID()).stream().map(User::ID).toArray(Long[]::new);
        if (response.isEmpty()) {
            LinkUpdate update =  new LinkUpdate(
                gitHubLink.ID(),
                gitHubLink.url(),
                "Link "
                    + gitHubLink.url()
                    + "already does not exists or invalid. It was removed from tracklist.",
                usersOfLink
            );
            linkService.remove(gitHubLink.ID());
            return update;
        }
        LinkUpdate update = new LinkUpdate(
            gitHubLink.ID(),
            gitHubLink.url(),
//            gitHubResponseProcessor.process(response.get());
            "Link " + gitHubLink.url() + " updated at :" + response.get().updatedAt(),
            usersOfLink
        );
        linkService.update(gitHubLink.ID(), response.get().updatedAt());
        return update;
    }

    private GitHubRepo parseRepo(String link) throws URISyntaxException {
        URI url = new URI(link);
        String[] pathValues = url.getPath().split("/");
        return new GitHubRepo(pathValues[1], pathValues[2]);
    }
}
