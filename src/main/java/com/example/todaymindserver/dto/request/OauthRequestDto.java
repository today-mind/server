package com.example.todaymindserver.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OauthRequestDto(@NotBlank String accessToken) {

}
