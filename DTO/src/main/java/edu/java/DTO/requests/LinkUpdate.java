package edu.java.DTO.requests;

public record LinkUpdate(long id, String url, String description, Long[] tgChatIds) {}
