package com.example.todaymindserver.common.client.dto;

import com.example.todaymindserver.dto.Message;

public record ClovaResponse(
    Status status,
    Result result
)
{
    public record Status(
       String code,
       String message
    ){}

    public record Result(
       Message message,
       Integer inputLength,
       Integer outputLength,
       String stopReason,
       long seed
    ){}
}
