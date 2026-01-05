package com.example.todaymindserver.common.client.oauth.dto;

public record GoogleUserResponse(
    String sub,
    String email
) {}