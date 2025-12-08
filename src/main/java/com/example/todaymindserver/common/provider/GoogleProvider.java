package com.example.todaymindserver.common.provider;

import org.springframework.stereotype.Component;

import com.example.todaymindserver.common.client.google.GoogleOauthClient;
import com.example.todaymindserver.common.client.dto.GoogleUserResponse;
import com.example.todaymindserver.common.util.OauthProviderType;
import com.example.todaymindserver.dto.request.OauthRequestDto;
import com.example.todaymindserver.dto.response.OauthUserInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleProvider implements OauthProvider {

    private final GoogleOauthClient googleOauthClient;

    @Override
    public OauthProviderType getProviderType() {
        return OauthProviderType.GOOGLE;
    }

    /**
     * <p><b>Google 응답 규칙</b></p>
     * <ul>
     * <li>'sub' : Google 내부에서 유일한 사용자 식별자 (Provider 단위 Unique)</li>
     * <li>'email' : null일 수도 아닐 수도 있음 (식별자가 아닌 속성 값)</li>
     * </ul>
     * </p>
     */
    @Override
    public OauthUserInfo getUserInfoFromOauthServer(OauthRequestDto request) {

        GoogleUserResponse googleUserInfo = googleOauthClient.getGoogleUserInfo(request.accessToken());

        String sub = googleUserInfo.sub();
        String email = googleUserInfo.email();

        return new OauthUserInfo(
            sub,
            email
        );
    }
}
