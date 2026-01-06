package com.example.todaymindserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todaymindserver.common.client.oauth.provider.OauthProvider;
import com.example.todaymindserver.common.util.JwtProvider;
import com.example.todaymindserver.common.client.oauth.OauthProviderFactory;
import com.example.todaymindserver.domain.oauth.OauthProviderType;
import com.example.todaymindserver.domain.user.UserRejoinPolicy;
import com.example.todaymindserver.dto.request.OauthRequestDto;
import com.example.todaymindserver.dto.response.LoginResponseDto;
import com.example.todaymindserver.dto.response.OauthUserInfo;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final UserRejoinPolicy userRejoinPolicy;
    private final OauthProviderFactory providerFactory;
    private final JwtProvider jwtProvider;

    @Transactional
    public LoginResponseDto signUpOrLoginFromOauth(
        OauthProviderType oauthProviderType,
        OauthRequestDto request
    ) {
        // 1. oauth provider 확인 후, 알맞은 oauth로 사용자 정보 가져오기
        OauthProvider oauthProvider = providerFactory.getProvider(oauthProviderType);
        OauthUserInfo userInfo = oauthProvider.getUserInfoFromOauthServer(request);

        // 2. 회원 탈퇴 정책 확인
        userRejoinPolicy.validateRejoinAllowed(oauthProviderType, userInfo.sub());

        // 3. 사용자 생성
        User user = findOrCreateUserBy(oauthProviderType, userInfo.sub(), userInfo.email());

        // 4. 토큰 발행
        String accessToken = jwtProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

        // 5. DB에 세션(refresh token) 저장
        refreshTokenService.issueInitialToken(user, refreshToken);

        return new LoginResponseDto(
            accessToken,
            refreshToken,
            user.getEmail(),
            user.getNickName()
        );
    }

    /**
     * provider + sub 조합은 해당 Provider 내에서 유일한 사용자 식별자이므로,
     * 이를 기준으로 기존 계정을 조회하고, 없을 경우 신규 계정을 생성한다.
     *
     * <p>
     * sub는 OAuth provider가 보증하는 unique한 값이며 email은 변경 가능성이 있어
     * 계정 기본 식별자로 사용하지 않는다.
     * </p>
     */
    private User findOrCreateUserBy(OauthProviderType provider, String sub, String email) {
        return userRepository.findByProviderAndProviderUserId(provider, sub)
            .orElseGet(() -> userRepository.save(User.create(email, provider, sub)));
    }
}
