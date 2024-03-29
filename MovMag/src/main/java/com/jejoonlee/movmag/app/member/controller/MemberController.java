package com.jejoonlee.movmag.app.member.controller;

import com.jejoonlee.movmag.app.member.dto.MemberLogin;
import com.jejoonlee.movmag.app.member.dto.MemberRegister;
import com.jejoonlee.movmag.app.member.dto.TokenDto;
import com.jejoonlee.movmag.app.member.security.TokenProvider;
import com.jejoonlee.movmag.app.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value="회원가입")
    public MemberRegister.Response register(
            @RequestBody @Valid MemberRegister.Request request
    ) {

        return memberService.register(request);
    }

    // http://localhost:8080/member/login
    @PostMapping("/login")
    @ApiOperation(value="로그인")
    public TokenDto login(
            @RequestBody  @Valid MemberLogin.Request request
    ) {

        MemberLogin.Response loginInfo = memberService.login(request);

        TokenDto token = TokenDto.builder()
                .accessToken(tokenProvider.generateToken(
                        loginInfo.getMemberId(),
                        loginInfo.getEmail(),
                        loginInfo.getRole()))
                .tokenType("Bearer")
                .build();

        return token;
    }
}
