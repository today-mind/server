package com.example.todaymindserver.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todaymindserver.dto.request.RefreshTokenRequestDto;
import com.example.todaymindserver.dto.response.RefreshTokenResponseDto;
import com.example.todaymindserver.service.RefreshTokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/token/refresh")
    public RefreshTokenResponseDto getAccessToken(
        @RequestBody @Valid RefreshTokenRequestDto request
    ) {
        return refreshTokenService.getNewRefreshTokenAndAccessToken(request);
    }
}
