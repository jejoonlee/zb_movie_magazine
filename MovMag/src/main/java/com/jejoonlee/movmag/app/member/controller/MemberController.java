package com.jejoonlee.movmag.app.member.controller;

import com.jejoonlee.movmag.app.member.dto.MemberLogin;
import com.jejoonlee.movmag.app.member.dto.MemberRegister;
import com.jejoonlee.movmag.app.member.security.TokenProvider;
import com.jejoonlee.movmag.app.member.service.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberServiceImpl;

    private final TokenProvider tokenProvider;

    // http://localhost:8080/member/register
    @PostMapping("/register")
    public MemberRegister.Response register(
            @RequestBody @Valid MemberRegister.Request request
            ) {

        return memberServiceImpl.register(request);
    }

    // http://localhost:8080/member/login
    @PostMapping("/login")
    public String login(
            @RequestBody  @Valid MemberLogin.Request request
    ) {

        MemberLogin.Response loginInfo = memberServiceImpl.login(request);

        return "토큰이 생성되었습니다 : Bearer " + tokenProvider.generateToken(loginInfo.getEmail(), loginInfo.getRole());
    }
}
