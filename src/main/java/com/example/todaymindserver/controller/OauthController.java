package com.example.todaymindserver.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todaymindserver.common.util.OauthProviderType;
import com.example.todaymindserver.dto.request.OauthRequestDto;
import com.example.todaymindserver.dto.response.LoginResponseDto;
import com.example.todaymindserver.service.OauthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @PostMapping("/{provider}/login")
    public LoginResponseDto signupOrLoginForOauth(
        @PathVariable String provider,
        @RequestBody @Valid OauthRequestDto request
    ) {
        return oauthService.signUpOrLoginFromOauth(
            OauthProviderType.from(provider),
            request
        );
    }
}
