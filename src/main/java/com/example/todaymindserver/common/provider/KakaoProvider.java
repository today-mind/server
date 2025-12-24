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

        // 닉네임 없이 id와 email만 전달
        return new OauthUserInfo(kakaoUserInfo.id().toString(), kakaoUserInfo.kakaoAccount().email());
    }
}