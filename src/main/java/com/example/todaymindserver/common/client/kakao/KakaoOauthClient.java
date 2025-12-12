package com.example.todaymindserver.common.client.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.todaymindserver.common.client.dto.KakaoUserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoOauthClient {

    private final RestClient restClient = RestClient.create();

    @Value("${oauth.kakao.user-info-url}")
    private String kakaoUserInfoUrl;

    public KakaoUserResponse getKakaoUserInfo (String accessToken) {

        KakaoUserResponse response = restClient.get()
            .uri(kakaoUserInfoUrl)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .onStatus(HttpStatusCode::isError, (req, res) -> {
                log.error("Kakao 사용자 프로필 API 요청 중 오류가 발생하였습니다: {}", res.getStatusCode());
                throw new RuntimeException("Kakao 사용자 프로필 API 조회 실패");
            })
            .body(KakaoUserResponse.class);

        if (response == null) {
            log.error("Kakao 사용자 정보가 누락되었습니다.");
            throw new RuntimeException("사용자 정보 로딩 중 오류가 발생하였습니다.");
        }
        if (response.id() == null) {
            log.error("Kakao 사용자 정보 중 고유 식별자(id)가 누락되었습니다.");
            throw new RuntimeException("사용자 정보 로딩 중 오류가 발생하였습니다.");
        }

        return response;
    }
}
