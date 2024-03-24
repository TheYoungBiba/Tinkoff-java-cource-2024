package edu.java.scrapper.domain.DTO;

import java.time.OffsetDateTime;

public record Link(Long ID, String url, OffsetDateTime updatedAt, OffsetDateTime checkedAt) {}
