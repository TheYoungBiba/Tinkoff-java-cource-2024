package edu.java.scrapper.clients;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.stackOverflowClient.StackOverflowQuestionClient;
import edu.java.scrapper.stackOverflowClient.StackOverflowQuestionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StackOverflowClientTest {
    private WireMockServer server;
    private StackOverflowQuestionClient client;

    @Test
    public void fetchRepoStandardTest() {
        long questionId = 11227809;
        String jsonResponse = """
            {
                "items": [
                    {
                        "tags": [
                            "java",
                            "c++",
                            "performance",
                            "cpu-architecture",
                            "branch-prediction"
                        ],
                        "owner": {
                            "account_id": 31692,
                            "reputation": 498256,
                            "user_id": 87234,
                            "user_type": "registered",
                            "accept_rate": 100,
                            "profile_image": "https://i.stack.imgur.com/FkjBe.png?s=256&g=1",
                            "display_name": "GManNickG",
                            "link": "https://stackoverflow.com/users/87234/gmannickg"
                        },
                        "is_answered": true,
                        "view_count": 1869306,
                        "protected_date": 1399067470,
                        "accepted_answer_id": 11227902,
                        "answer_count": 25,
                        "score": 27209,
                        "last_activity_date": 1706021923,
                        "creation_date": 1340805096,
                        "last_edit_date": 1701123268,
                        "question_id": 11227809,
                        "content_license": "CC BY-SA 4.0",
                        "link": "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-processing-an-unsorted-array",
                        "title": "Why is processing a sorted array faster than processing an unsorted array?"
                    }
                ],
                "has_more": false,
                "quota_max": 300,
                "quota_remaining": 278
            }""";
        stubServer(questionId, jsonResponse);
        StackOverflowQuestionResponse referent = new StackOverflowQuestionResponse(
            new StackOverflowQuestionResponse.Owner(
                "https://i.stack.imgur.com/FkjBe.png?s=256&g=1",
                "GManNickG",
                "https://stackoverflow.com/users/87234/gmannickg"
            ),
            true,
            1869306,
            25,
            27209,
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1706021923), ZoneId.of("UTC")),
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1340805096), ZoneId.of("UTC")),
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1701123268), ZoneId.of("UTC")),
            11227809,
            "https://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-processing-an-unsorted-array",
            "Why is processing a sorted array faster than processing an unsorted array?"
        );
        StackOverflowQuestionResponse testCase = client.fetchQuestion(questionId).get();
        assertEquals(testCase, referent);
    }

    @Test
    public void fetchRepoInvalidJsonTest() {
        long questionId = 11227809;
        String invalidJsonResponse = "{invalid response}";
        stubServer(questionId, invalidJsonResponse);
        assertTrue(client.fetchQuestion(questionId).isEmpty());
    }

    @Test
    public void fetchRepoResponseCode404Test() {
        long questionId = -11227809;
        server.stubFor(get(urlEqualTo(String.format("/%s?order=desc&sort=activity&site=stackoverflow", questionId)))
            .willReturn(aResponse().withStatus(HttpStatus.NOT_FOUND.value()).withBody("Not Found")));
        assertTrue(client.fetchQuestion(questionId).isEmpty());
    }

    @BeforeEach
    void clientServerStart() {
        server = new WireMockServer();
        server.start();
        WireMock.configureFor("localhost", server.port());
        client = new StackOverflowQuestionClient("http://localhost:" + server.port());
    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    private void stubServer(long questionId, String jsonResponse) {
        server.stubFor(get(String.format("/%s?order=desc&sort=activity&site=stackoverflow", questionId))
            .willReturn(aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(jsonResponse))
        );
    }
}
