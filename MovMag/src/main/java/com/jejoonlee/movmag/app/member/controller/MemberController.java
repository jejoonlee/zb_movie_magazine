package com.jejoonlee.movmag.app.member.controller;

import com.jejoonlee.movmag.app.member.dto.MemberLogin;
import com.jejoonlee.movmag.app.member.dto.MemberRegister;
import com.jejoonlee.movmag.app.member.dto.TokenDto;
import com.jejoonlee.movmag.app.member.security.TokenProvider;
import com.jejoonlee.movmag.app.member.service.MemberService;
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

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    // http://localhost:8080/member/register
    @PostMapping("/register")
    public MemberRegister.Response register(
            @RequestBody @Valid MemberRegister.Request request
    ) {

        return memberService.register(request);
    }

    // http://localhost:8080/member/login
    @PostMapping("/login")
    public TokenDto.Response login(
            @RequestBody  @Valid MemberLogin.Request request
    ) {

        MemberLogin.Response loginInfo = memberService.login(request);

        TokenDto.Response token = TokenDto.Response.builder()
                .tokenType("Bearer")
                .tokenInfo(tokenProvider.loginGenerateTokens(
                        loginInfo.getMemberId(),
                        loginInfo.getEmail(),
                        loginInfo.getRole()
                ))
                .build();

        return token;
    }
}
