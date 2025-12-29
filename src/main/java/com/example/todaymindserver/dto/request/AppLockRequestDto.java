package com.example.todaymindserver.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppLockRequestDto {

    @NotNull(message = "비밀번호 필드는 필수입니다.")
    @Pattern(regexp = "^(\\d{4}|)$", message = "비밀번호는 숫자 4자리 또는 빈 값이어야 합니다.")
    private String password;
}