package edu.java.scrapper.clients;

import java.time.OffsetDateTime;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.gitHubClient.GitHubRepoClient;
import edu.java.scrapper.gitHubClient.GitHubRepoResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GitHubRepoClientTest {
    private WireMockServer server;
    private GitHubRepoClient client;

    @Test
    public void fetchRepoStandardTest() {
        String repoOwner = "TheYoungBiba";
        String repoName = "Tinkoff-java-cource-2024";
        String jsonResponse = """
            {
              "id": 750436695,
              "node_id": "R_kgDOLLrBVw",
              "name": "Tinkoff-java-cource-2024",
              "full_name": "TheYoungBiba/Tinkoff-java-cource-2024",
              "private": false,
              "owner": {
                "login": "TheYoungBiba",
                "id": 131690011,
                "node_id": "U_kgDOB9luGw",
                "avatar_url": "https://avatars.githubusercontent.com/u/131690011?v=4",
                "gravatar_id": "",
                "url": "https://api.github.com/users/TheYoungBiba",
                "html_url": "https://github.com/TheYoungBiba",
                "followers_url": "https://api.github.com/users/TheYoungBiba/followers",
                "following_url": "https://api.github.com/users/TheYoungBiba/following{/other_user}",
                "gists_url": "https://api.github.com/users/TheYoungBiba/gists{/gist_id}",
                "starred_url": "https://api.github.com/users/TheYoungBiba/starred{/owner}{/repo}",
                "subscriptions_url": "https://api.github.com/users/TheYoungBiba/subscriptions",
                "organizations_url": "https://api.github.com/users/TheYoungBiba/orgs",
                "repos_url": "https://api.github.com/users/TheYoungBiba/repos",
                "events_url": "https://api.github.com/users/TheYoungBiba/events{/privacy}",
                "received_events_url": "https://api.github.com/users/TheYoungBiba/received_events",
                "type": "User",
                "site_admin": false
              },
              "html_url": "https://github.com/TheYoungBiba/Tinkoff-java-cource-2024",
              "description": null,
              "fork": false,
              "url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024",
              "forks_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/forks",
              "keys_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/keys{/key_id}",
              "collaborators_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/collaborators{/collaborator}",
              "teams_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/teams",
              "hooks_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/hooks",
              "issue_events_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/issues/events{/number}",
              "events_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/events",
              "assignees_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/assignees{/user}",
              "branches_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/branches{/branch}",
              "tags_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/tags",
              "blobs_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/git/blobs{/sha}",
              "git_tags_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/git/tags{/sha}",
              "git_refs_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/git/refs{/sha}",
              "trees_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/git/trees{/sha}",
              "statuses_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/statuses/{sha}",
              "languages_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/languages",
              "stargazers_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/stargazers",
              "contributors_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/contributors",
              "subscribers_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/subscribers",
              "subscription_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/subscription",
              "commits_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/commits{/sha}",
              "git_commits_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/git/commits{/sha}",
              "comments_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/comments{/number}",
              "issue_comment_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/issues/comments{/number}",
              "contents_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/contents/{+path}",
              "compare_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/compare/{base}...{head}",
              "merges_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/merges",
              "archive_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/{archive_format}{/ref}",
              "downloads_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/downloads",
              "issues_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/issues{/number}",
              "pulls_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/pulls{/number}",
              "milestones_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/milestones{/number}",
              "notifications_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/notifications{?since,all,participating}",
              "labels_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/labels{/name}",
              "releases_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/releases{/id}",
              "deployments_url": "https://api.github.com/repos/TheYoungBiba/Tinkoff-java-cource-2024/deployments",
              "created_at": "2024-01-30T16:37:31Z",
              "updated_at": "2024-02-09T12:43:27Z",
              "pushed_at": "2024-02-18T15:15:10Z",
              "git_url": "git://github.com/TheYoungBiba/Tinkoff-java-cource-2024.git",
              "ssh_url": "git@github.com:TheYoungBiba/Tinkoff-java-cource-2024.git",
              "clone_url": "https://github.com/TheYoungBiba/Tinkoff-java-cource-2024.git",
              "svn_url": "https://github.com/TheYoungBiba/Tinkoff-java-cource-2024",
              "homepage": null,
              "size": 111,
              "stargazers_count": 0,
              "watchers_count": 0,
              "language": "Java",
              "has_issues": true,
              "has_projects": true,
              "has_downloads": true,
              "has_wiki": false,
              "has_pages": false,
              "has_discussions": false,
              "forks_count": 0,
              "mirror_url": null,
              "archived": false,
              "disabled": false,
              "open_issues_count": 1,
              "license": null,
              "allow_forking": true,
              "is_template": false,
              "web_commit_signoff_required": false,
              "topics": [

              ],
              "visibility": "public",
              "forks": 0,
              "open_issues": 1,
              "watchers": 0,
              "default_branch": "main",
              "temp_clone_token": null,
              "network_count": 0,
              "subscribers_count": 1
            }""";
        stubServer(repoOwner, repoName, jsonResponse);
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
        GitHubRepoResponse testCase = client.fetchRepository(repoOwner, repoName).get();
        assertEquals(testCase, referent);
    }

    @Test
    public void fetchRepoInvalidJsonTest() {
        String repoOwner = "TheYoungBiba";
        String repoName = "Tinkoff-java-cource-2024";
        String invalidJsonResponse = "{invalid response}";
        stubServer(repoOwner, repoName, invalidJsonResponse);
        assertTrue(client.fetchRepository(repoOwner, repoName).isEmpty());
    }

    @Test
    public void fetchRepoResponseCode404Test() {
        String repoOwner = "qwerty";
        String repoName = "qwerty";
        server.stubFor(get(urlEqualTo(String.format("/%s/%s", repoOwner, repoName)))
                .willReturn(aResponse().withStatus(HttpStatus.NOT_FOUND.value()).withBody("Not Found")));
        assertTrue(client.fetchRepository(repoOwner, repoName).isEmpty());
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
}
