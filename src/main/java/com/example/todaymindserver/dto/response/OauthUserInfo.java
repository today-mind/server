package com.example.todaymindserver.dto.response;

/**
 * OAuth 서버로부터 받은 사용자 정보를 담는 DTO
 * [수정] nickname을 record의 정식 파라미터로 추가하여 빌드 에러 해결 및 데이터 확장성 확보
 */
public record OauthUserInfo(
        String sub,
        String email,
        String nickname
) {
    // 이제 별도의 public String nickname() { return null; } 메서드는 필요 없습니다.
    // record가 자동으로 getter 메서드를 생성해 줍니다.
}