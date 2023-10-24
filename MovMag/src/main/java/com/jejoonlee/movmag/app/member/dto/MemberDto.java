package com.jejoonlee.movmag.app.member.dto;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {

    private String email;
    private String username;
    private String password;
    private String phoneNum;
    private String role;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

    public static MemberDto fromEntity(MemberEntity memberEntity) {
        return MemberDto.builder()
                .email(memberEntity.getEmail())
                .username(memberEntity.getUsername())
                .password(memberEntity.getPassword())
                .phoneNum(memberEntity.getPhoneNum())
                .role(memberEntity.getRole())
                .registeredAt(memberEntity.getRegisteredAt())
                .updatedAt(memberEntity.getUpdatedAt())
                .build();
    }

}
