package edu.java.scrapper.clients;

import java.io.IOException;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.gitHubClient.GitHubRepo;
import edu.java.scrapper.gitHubClient.GitHubRepoClient;
import edu.java.scrapper.gitHubClient.GitHubRepoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubRepoClientTest {
    private WireMockServer server;
    private GitHubRepoClient client;

    @Test
    public void fetchRepoStandardTest() throws IOException {
        String repoOwner = "TheYoungBiba";
        String repoName = "Tinkoff-java-cource-2024";
        Path pathToJson = Path.of("src/test/java/edu/java/scrapper/clients/GitHubRepoClientTestJsonResponse.json");
        stubServer(repoOwner, repoName, pathToJson);
        GitHubRepoResponse referent = new GitHubRepoResponse(
            750436695,
            "Tinkoff-java-cource-2024",
            false,
            new GitHubRepoResponse.Owner(
                "TheYoungBiba",
                131690011,
                "https://avatars.githubusercontent.com/u/131690011?v=4",
                "https://github.com/TheYoungBiba"
            ),
            "https://github.com/TheYoungBiba/Tinkoff-java-cource-2024",
            null,
            OffsetDateTime.parse("2024-01-30T16:37:31Z"),
            OffsetDateTime.parse("2024-02-09T12:43:27Z"),
            OffsetDateTime.parse("2024-02-18T15:15:10Z"),
            "Java"
        );
        GitHubRepoResponse testCase = client.fetchRepository(new GitHubRepo(repoOwner, repoName)).get();
        assertEquals(testCase, referent);
    }

    @Test
    public void fetchRepoInvalidJsonTest() {
        String repoOwner = "TheYoungBiba";
        String repoName = "Tinkoff-java-cource-2024";
        String invalidJsonResponse = "{invalid response}";
        stubServer(repoOwner, repoName, invalidJsonResponse);
        assertTrue(client.fetchRepository(new GitHubRepo(repoOwner, repoName)).isEmpty());
    }

    @Test
    public void fetchRepoResponseCode404Test() {
        String repoOwner = "qwerty";
        String repoName = "qwerty";
        server.stubFor(get(urlEqualTo(String.format("/%s/%s", repoOwner, repoName)))
                .willReturn(aResponse().withStatus(HttpStatus.NOT_FOUND.value()).withBody("Not Found")));
        assertTrue(client.fetchRepository(new GitHubRepo(repoOwner, repoName)).isEmpty());
    }

    @BeforeEach
    void clientServerStart() {
        server = new WireMockServer();
        server.start();
        WireMock.configureFor("localhost", server.port());
        client = new GitHubRepoClient("http://localhost:" + server.port());
    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    private void stubServer(String repoOwner, String repoName, String jsonResponse) {
        server.stubFor(
            get(urlEqualTo(String.format("/%s/%s", repoOwner, repoName)))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(jsonResponse))
        );
    }

    private void stubServer(String repoOwner, String repoName, Path pathToJson) throws IOException {
        server.stubFor(
            get(urlEqualTo(String.format("/%s/%s", repoOwner, repoName)))
                .willReturn(aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withJsonBody(new ObjectMapper().readTree(pathToJson.toFile())))
        );
    }
}
