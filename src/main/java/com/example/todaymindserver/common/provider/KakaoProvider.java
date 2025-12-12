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

    /**
     * <p><b>Kakao 응답 규칙</b></p>
     * <ul>
     * <li>'sub' : Kakao 내부에서 유일한 사용자 식별자 (Provider 단위 Unique)</li>
     * <li>'email' : null일 수도 아닐 수도 있음 (식별자가 아닌 속성 값)</li>
     * </ul>
     * </p>
     */
    @Override
    public OauthUserInfo getUserInfoFromOauthServer(OauthRequestDto request) {

        KakaoUserResponse kakaoUserInfo = kakaoOauthClient.getKakaoUserInfo(request.accessToken());

        String sub = kakaoUserInfo.id().toString();
        String email = kakaoUserInfo.kakaoAccount().email();

        return new OauthUserInfo(
            sub,
            email
        );
    }
}
