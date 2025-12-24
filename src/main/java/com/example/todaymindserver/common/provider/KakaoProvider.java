package com.example.todaymindserver.common.provider;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.common.client.kakao.KakaoOauthClient;
import com.example.todaymindserver.common.client.dto.KakaoUserResponse;
import com.example.todaymindserver.common.util.OauthProviderType;
import com.example.todaymindserver.dto.request.OauthRequestDto;
import com.example.todaymindserver.dto.response.OauthUserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProvider implements OauthProvider {

    private final KakaoOauthClient kakaoOauthClient;

    @Override
    public OauthProviderType getProviderType() {
        return OauthProviderType.KAKAO;
    }

    @Override
    public OauthUserInfo getUserInfoFromOauthServer(OauthRequestDto request) {

        KakaoUserResponse kakaoUserInfo = kakaoOauthClient.getKakaoUserInfo(request.accessToken());

        String sub = kakaoUserInfo.id().toString();
        String email = kakaoUserInfo.kakaoAccount().email();

        // [수정] 빨간 줄 해결: KakaoUserResponse의 계층 구조에 맞춰 nickname을 가져옵니다.
        String nickname = (kakaoUserInfo.kakaoAccount() != null && kakaoUserInfo.kakaoAccount().profile() != null)
                ? kakaoUserInfo.kakaoAccount().profile().nickname()
                : null;

        // [최종] OauthUserInfo 생성자 인자 3개(sub, email, nickname)를 맞춥니다.
        return new OauthUserInfo(
                sub,
                email,
                nickname
        );
    }
}