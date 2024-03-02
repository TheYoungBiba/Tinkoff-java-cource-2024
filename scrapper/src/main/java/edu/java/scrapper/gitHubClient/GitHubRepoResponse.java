package edu.java.scrapper.gitHubClient;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubRepoResponse(
    long id,
    String name,
    @JsonProperty("private")
    boolean isPrivate,
    Owner owner,
    @JsonProperty("html_url")
    String htmlUrl,
    String description,
    @JsonProperty("created_at")
    OffsetDateTime createdAt,
    @JsonProperty("updated_at")
    OffsetDateTime updatedAt,
    @JsonProperty("pushed_at")
    OffsetDateTime pushedAt,
    String language
) {
    public record Owner(
        String login,
        long id,
        @JsonProperty("avatar_url")
        String avatarUrl,
        @JsonProperty("html_url")
        String htmlUrl
    ) {}
}
