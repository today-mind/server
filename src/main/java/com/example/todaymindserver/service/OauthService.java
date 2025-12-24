package com.example.todaymindserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todaymindserver.common.provider.OauthProvider;
import com.example.todaymindserver.common.util.JwtProvider;
import com.example.todaymindserver.common.factory.OauthProviderFactory;
import com.example.todaymindserver.common.util.OauthProviderType;
import com.example.todaymindserver.dto.request.OauthRequestDto;
import com.example.todaymindserver.dto.response.LoginResponseDto;
import com.example.todaymindserver.dto.response.OauthUserInfo;
import com.example.todaymindserver.entity.User;
import com.example.todaymindserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final OauthProviderFactory providerFactory;
    private final JwtProvider jwtProvider;

    /**
     * OAuth 로그인 혹은 회원가입을 처리하는 핵심 비즈니스 흐름.
     */
    @Transactional
    public LoginResponseDto signUpOrLoginFromOauth(
            OauthProviderType oauthProviderType,
            OauthRequestDto request
    ) {
        OauthProvider oauthProvider = providerFactory.getProvider(oauthProviderType);
        OauthUserInfo userInfo = oauthProvider.getUserInfoFromOauthServer(request);

        // [수정] 닉네임 정보를 함께 전달하도록 변경
        User user = findOrCreateUserBy(oauthProviderType, userInfo.sub(), userInfo.email(), userInfo.nickname());

        String accessToken = jwtProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

        refreshTokenService.saveOrUpdate(user.getUserId(), refreshToken);

        return new LoginResponseDto(
                accessToken,
                refreshToken,
                user.getEmail(),
                user.getNickName()
        );
    }

    /**
     * [수정] findOrCreateUserBy 메서드 시그니처 변경 및 User.create 인자 추가
     * sub는 OAuth provider가 보증하는 unique한 값이며 email은 변경 가능성이 있어
     * 계정 기본 식별자로 사용하지 않는다.
     */
    private User findOrCreateUserBy(OauthProviderType provider, String sub, String email, String nickname) {
        return userRepository.findByProviderAndProviderUserId(provider, sub)
                .orElseGet(() -> {
                    // 닉네임이 없으면 이메일 앞자리 사용 (Fallback)
                    String finalNickname = (nickname != null && !nickname.isBlank())
                            ? nickname
                            : (email != null ? email.split("@")[0] : "사용자");

                    // User.create(email, provider, sub, finalNickname) 호출 (4개 인자)
                    return userRepository.save(User.create(email, provider, sub, finalNickname));
                });
    }
}