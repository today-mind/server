package com.example.todaymindserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 앱 잠금 비밀번호 요청 DTO (4자리 숫자)
 */
@Getter
@NoArgsConstructor
public class AppLockRequestDto {

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^\\d{4}$", message = "비밀번호는 숫자 4자리여야 합니다.")
    private String password;
}