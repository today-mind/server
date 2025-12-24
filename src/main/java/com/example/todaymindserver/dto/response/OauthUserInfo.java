package com.example.todaymindserver.dto.response;

public record OauthUserInfo(String sub, String email) {
    public String nickname() {
        return null;
    }
}