package com.example.todaymindserver.common.client.dto;

public record GoogleUserResponse(
    String sub,
    String email
) {}