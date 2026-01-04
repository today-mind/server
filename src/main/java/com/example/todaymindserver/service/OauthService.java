package com.example.todaymindserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todaymindserver.common.provider.OauthProvider;
import com.example.todaymindserver.common.util.JwtProvider;
import com.example.todaymindserver.common.factory.OauthProviderFactory;
import com.example.todaymindserver.domain.BusinessException;
import com.example.todaymindserver.domain.oauth.OauthProviderType;
import com.example.todaymindserver.domain.user.UserErrorCode;
import com.example.todaymindserver.dto.request.OauthRequestDto;
import com.example.todaymindserver.dto.response.LoginResponseDto;
import com.example.todaymindserver.dto.response.OauthUserInfo;
import com.example.todaymindserver.domain.user.User;
import com.example.todaymindserver.repository.UserRepository;
import com.example.todaymindserver.repository.UserWithdrawalHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final UserWithdrawalHistoryRepository userWithdrawalHistoryRepository;
    private final OauthProviderFactory providerFactory;
    private final JwtProvider jwtProvider;

    @Value("${policy.rejoin-cooldown-hours}")
    private int rejoinCooldownHours;

    /**
     * OAuth 로그인 혹은 회원가입을 처리하는 핵심 비즈니스 흐름.
     *
     * <p>
     * Flow:
     * <ol>
     *   <li>요청된 Provider 타입에 맞는 Provider 구현체 선택 (Provider 패턴)</li>
     *   <li>OAuth 서버로부터 사용자 정보 조회</li>
     *   <li>provider + sub 조합으로 기존 사용자 조회 또는 신규 사용자 생성</li>
     *   <li>AccessToken 및 RefreshToken 발급</li>
     *   <li>RefreshToken Rotation 정책에 따라 RefreshToken 저장/갱신</li>
     * </ol>
     * </p>
     *
     * <p>
     * 설계 의도:
     * <ul>
     *   <li>Provider마다 응답 구조가 다르기 때문에 ProviderFactory를 통해 파싱 로직을 캡슐화</li>
     *   <li>sub는 OAuth Provider 내에서 유일한 식별자이므로, provider + sub 조합을 계정 매핑 기준으로 사용</li>
     *   <li>RefreshToken은 매 로그인마다 Rotation하여 보안성과 통제력을 확보</li>
     * </ul>
     * </p>
     */
    @Transactional
    public LoginResponseDto signUpOrLoginFromOauth(
        OauthProviderType oauthProviderType,
        OauthRequestDto request
    ) {
        OauthProvider oauthProvider = providerFactory.getProvider(oauthProviderType);
        OauthUserInfo userInfo = oauthProvider.getUserInfoFromOauthServer(request);

        userWithdrawalHistoryRepository.findByProviderAndProviderUserId(
            oauthProviderType,
            userInfo.sub()
        ).ifPresent(history -> {
            if(history.isCooldownActive(rejoinCooldownHours)) {
                throw new BusinessException(
                    UserErrorCode.REJOIN_COOLDOWN_ACTIVE,
                    String.format("회원 탈퇴 후 %s시간이 지나야 다시 가입할 수 있습니다.", rejoinCooldownHours)
                );

            }
        });

        User user = findOrCreateUserBy(oauthProviderType, userInfo.sub(), userInfo.email());

        String accessToken = jwtProvider.createAccessToken(user.getUserId());
        String refreshToken = jwtProvider.createRefreshToken(user.getUserId());

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
