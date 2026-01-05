package com.example.todaymindserver.common.client.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import com.example.todaymindserver.common.client.oauth.dto.KakaoUserResponse;
import com.example.todaymindserver.common.client.util.SafeBody;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.oauth.OauthErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOauthClient {

    private final RestClient oauthRestClient;

    @Value("${oauth.kakao.user-info-url}")
    private String kakaoUserInfoUrl;

    public KakaoUserResponse getKakaoUserInfo (String accessToken) {

        KakaoUserResponse response = oauthRestClient.get()
            .uri(kakaoUserInfoUrl)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                log.warn(
                    "[OAUTH][4xx] status={}, body={}",
                    res.getStatusCode(),
                    SafeBody.read(res)
                );
                throw new HttpClientErrorException(res.getStatusCode());
            })
            .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                log.error(
                    "[OAUTH][5xx] status={}, body={}",
                    res.getStatusCode(),
                    SafeBody.read(res)
                );
                throw new HttpServerErrorException(res.getStatusCode());
            })
            .body(KakaoUserResponse.class);

        if (response == null) {
            log.error("Kakao 사용자 정보가 누락되었습니다.");
            throw new BusinessException(OauthErrorCode.KAKAO_USER_INFO_NOT_FOUND);
        }
        if (response.id() == null) {
            log.error("Kakao 사용자 정보 중 고유 식별자(id)가 누락되었습니다.");
            throw new BusinessException(OauthErrorCode.KAKAO_USER_INFO_NOT_FOUND);
        }

        return response;
    }
}
