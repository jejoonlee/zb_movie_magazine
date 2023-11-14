package com.jejoonlee.movmag.app.member.dto;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.member.domain.MemberRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto implements UserDetails {

    private Long memberId;
    private String email;
    private String username;
    private String password;
    private String phoneNum;
    private MemberRole role;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

    public static MemberDto fromEntity(MemberEntity memberEntity) {
        return MemberDto.builder()
                .memberId(memberEntity.getMemberId())
                .email(memberEntity.getEmail())
                .username(memberEntity.getUsername())
                .password(memberEntity.getPassword())
                .phoneNum(memberEntity.getPhoneNum())
                .role(memberEntity.getRole())
                .registeredAt(memberEntity.getRegisteredAt())
                .updatedAt(memberEntity.getUpdatedAt())
                .build();
    }

    public static MemberEntity toEntity(MemberDto memberDto) {
        return MemberEntity.builder()
                .memberId(memberDto.getMemberId())
                .email(memberDto.getEmail())
                .username(memberDto.getUsername())
                .password(memberDto.getPassword())
                .phoneNum(memberDto.getPhoneNum())
                .role(memberDto.getRole())
                .registeredAt(memberDto.getRegisteredAt())
                .updatedAt(memberDto.getUpdatedAt())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<>();

        if (this.role == MemberRole.EDITOR) {
            auth.add(new SimpleGrantedAuthority("ROLE_EDITOR"));
        } else if (this.role == MemberRole.USER) {
            auth.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (this.role == MemberRole.ADMIN) {
            auth.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            throw new RuntimeException("없는 권한입니다");
        }

        return auth;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
