package com.jejoonlee.movmag.app.member.dto;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

public class MemberRegister {

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message="이메일은 필수 입력 사항입니다")
        @Email(message="이메일 형식에 맞지 않습니다",
        regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
        private String email;

        @NotBlank(message="회원 이름은 필수 입력 사항입니다")
        private String username;

        @NotBlank(message="비밀번호는 필수 입력 사항입니다")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^*+=-]).{8,100}$",
                message = "비밀번호는 8~16자 영문, 숫자, 특수문자를 사용하세요.")
        private String password;

        @NotBlank(message="전화번호는 필수 입력 사항입니다")
        @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
                message = "010-0000-0000 형식으로 입력해주세요")
        private String phoneNum;

        @NotBlank(message="Editor 또는 User를 입력해주세요")
        @Pattern(regexp = "Editor|User", message = "Editor 또는 User를 입력해주세요")
        private String role;

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .email(this.email)
                    .username(this.username)
                    .password(this.password)
                    .phoneNum(this.phoneNum)
                    .role(this.role)
                    .registeredAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String email;
        private String username;
        private String role;

        public static Response fromDto(MemberDto memberDto){
            return Response.builder()
                    .email(memberDto.getEmail())
                    .username(memberDto.getUsername())
                    .role(memberDto.getRole())
                    .build();
        }
    }

}
