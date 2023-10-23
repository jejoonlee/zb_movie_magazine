package com.jejoonlee.movmag.app.member.service.impl;

import com.jejoonlee.movmag.app.member.domain.MemberEntity;
import com.jejoonlee.movmag.app.member.dto.MemberDto;
import com.jejoonlee.movmag.app.member.dto.MemberRegister;
import com.jejoonlee.movmag.app.member.repository.MemberRepository;
import com.jejoonlee.movmag.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberRegister.Response register(MemberRegister.Request request) {

        boolean memberExist = memberRepository.existsByEmail(request.getEmail());

        if (memberExist) {
            throw new RuntimeException("입력한 이메일이 이미 존재합니다");
        }

        // 비밀번호 encoding 하기
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        // request에서 가지고 온 것 entity로 바꾸기
        MemberEntity memberEntity = request.toEntity();

        // entity를 db에 저장하기
        memberRepository.save(memberEntity);

        // response 객체를 만들기어서 return하기
        return MemberRegister.Response.fromDto(MemberDto.fromEntity(memberEntity));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다"));

    }
}
