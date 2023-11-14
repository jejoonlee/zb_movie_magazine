package com.jejoonlee.movmag.app.member.dto;

import com.jejoonlee.movmag.app.member.domain.MemberRole;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberLogin {

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message="이메일을 입력해주세요")
        @Email(message="이메일 형식이 아닙니다. 다시 입력해주세요")
        private String email;

        @NotBlank(message="비밀번호는 필수 입력 사항입니다")
        private String password;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {

        private Long memberId;
        private String email;
        private MemberRole role;

        public static MemberLogin.Response fromDto(MemberDto memberDto) {
            return Response.builder()
                    .memberId(memberDto.getMemberId())
                    .email(memberDto.getEmail())
                    .role(memberDto.getRole())
                    .build();
        }

    }
}
