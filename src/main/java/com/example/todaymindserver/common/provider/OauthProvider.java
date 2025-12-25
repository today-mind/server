package com.example.todaymindserver.common.provider;

import com.example.todaymindserver.domain.oauth.OauthProviderType;
import com.example.todaymindserver.dto.request.OauthRequestDto;
import com.example.todaymindserver.dto.response.OauthUserInfo;

/**
 *  <p><b>OAuth Provider.</b></p>
 *
 * <ul>
 * <li>OAuth 서버와 통신하여 사용자 정보를 조회한다.</li>
 * <li>Provider마다 응답 구조가 다르므로, 각 Provider는 자신의 파싱 로직을 담당한다.</li>
 * <li>조회한 응답은 서비스 레이어에서 공통적으로 사용할 수 있도록 OauthUserInfo 도메인 모델로 변환한다.</li>
 * </ul>
 */
public interface OauthProvider {
    OauthProviderType getProviderType();
    OauthUserInfo getUserInfoFromOauthServer(OauthRequestDto request);
}
