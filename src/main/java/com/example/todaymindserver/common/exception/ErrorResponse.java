package com.example.todaymindserver.common.exception;

import lombok.Builder;

@Builder
public record ErrorResponse(int status, String code, String message) {

}