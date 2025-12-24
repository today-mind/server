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

    private final UserRepository userRepository;
    private final OauthProviderFactory providerFactory;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public LoginResponseDto signUpOrLoginFromOauth(OauthProviderType oauthProviderType, OauthRequestDto request) {
        OauthProvider oauthProvider = providerFactory.getProvider(oauthProviderType);
        OauthUserInfo userInfo = oauthProvider.getUserInfoFromOauthServer(request);

        // [리뷰 반영] nickname 인자를 완전히 제거하고 롤백했습니다.
        User user = findOrCreateUserBy(oauthProviderType, userInfo.sub(), userInfo.email());

        String accessToken = jwtProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

        refreshTokenService.saveOrUpdate(user.getUserId(), refreshToken);

        return new LoginResponseDto(accessToken, refreshToken, user.getEmail(), user.getNickName());
    }

    /**
     * [리뷰 반영] 닉네임을 파라미터로 받지 않고, 내부 로직(이메일 기반)으로만 생성합니다.
     */
    private User findOrCreateUserBy(OauthProviderType provider, String sub, String email) {
        return userRepository.findByProviderAndProviderUserId(provider, sub)
                .orElseGet(() -> {
                    // 이메일이 있으면 앞자리, 없으면 "사용자"를 기본 닉네임으로 설정
                    String tempNickname = (email != null && !email.isBlank()) ? email.split("@")[0] : "사용자";
                    return userRepository.save(User.create(email, provider, sub, tempNickname));
                });
    }
}