package com.example.todaymindserver.common.client.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.todaymindserver.common.client.dto.GoogleUserResponse;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.oauth.OauthErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOauthClient {

    private final RestClient restClient = RestClient.create();

    @Value("${oauth.google.user-info-url}")
    private String googleUserInfoUrl;

    public GoogleUserResponse getGoogleUserInfo(String accessToken)  {

        GoogleUserResponse response = restClient.get()
            .uri(googleUserInfoUrl)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .onStatus(HttpStatusCode::isError, (req, res) -> {
                log.error("Google 사용자 프로필 API 요청 중 오류가 발생하였습니다: {}", res.getStatusCode());
            })
            .body(GoogleUserResponse.class);

        if (response == null) {
            log.error("Google OAuth 사용자 정보가 누락되었습니다.");
            throw new BusinessException(OauthErrorCode.GOOGLE_USER_INFO_NOT_FOUND);
        }
        if (response.sub() == null) {
            log.error("Google OAuth 사용자 정보 중 고유 식별자(sub)가 누락되었습니다.");
            throw new BusinessException(OauthErrorCode.GOOGLE_USER_INFO_NOT_FOUND);
        }

        return response;
    }
}
