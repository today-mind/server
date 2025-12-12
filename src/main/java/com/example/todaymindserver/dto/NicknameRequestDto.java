package com.example.todaymindserver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class NicknameRequestDto {

    // [기획 반영] 닉네임 규칙: 2~20자, 한글/영어/숫자만 허용 (특수문자 제외)
    @NotBlank(message = "닉네임은 필수 항목입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 설정해야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 특수문자를 포함할 수 없습니다.")
    private String nickname;
}