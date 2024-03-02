package edu.java.scrapper.gitHubClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GitHubRepoClient {
    @SuppressWarnings("MemberName")
    private final String BASE_BIT_HUB_URL = "https://api.github.com/repos";
    private final WebClient client;

    public GitHubRepoClient() {
        client = WebClient.create(BASE_BIT_HUB_URL);
    }

    public GitHubRepoClient(String baseURL) {
        client = WebClient.create(baseURL);
    }

    public Optional<GitHubRepoResponse> fetchRepository(String repoOwner, String repoName) {
        try {
            String json = client.get()
                .uri("/{owner}/{repo}", repoOwner, repoName)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return Optional.of(parseJson(json));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private GitHubRepoResponse parseJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(json, GitHubRepoResponse.class);
    }
}
