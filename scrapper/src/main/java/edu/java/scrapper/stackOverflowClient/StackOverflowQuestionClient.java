package edu.java.scrapper.stackOverflowClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StackOverflowQuestionClient {
    @SuppressWarnings("MemberName")
    private final String BASE_STACK_OVERFLOW_URL = "https://api.stackexchange.com/2.3/questions";
    private final WebClient client;

    public StackOverflowQuestionClient() {
        client = WebClient.create(BASE_STACK_OVERFLOW_URL);
    }

    public StackOverflowQuestionClient(String baseURL) {
        client = WebClient.create(baseURL);
    }

    public Optional<StackOverflowQuestionResponse> fetchQuestion(long questionId) {
        try {
            String json = client.get()
                .uri(uriBuilder -> uriBuilder.path("/{id}")
                    .queryParam("order", "desc")
                    .queryParam("sort", "activity")
                    .queryParam("site", "stackoverflow")
                    .build(questionId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            return Optional.of(parseJson(json));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private StackOverflowQuestionResponse parseJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode nodes = mapper.readTree(json);
        return mapper.treeToValue(nodes.get("items").get(0), StackOverflowQuestionResponse.class);
    }
}
