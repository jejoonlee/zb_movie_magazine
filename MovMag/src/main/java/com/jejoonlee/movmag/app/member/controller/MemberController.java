package com.jejoonlee.movmag.app.member.controller;

import com.jejoonlee.movmag.app.member.service.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.jejoonlee.movmag.app.member.dto.MemberRegister;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberServiceImpl;

    // http://localhost:8080/member/register
    @PostMapping("/register")
    public MemberRegister.Response register(
            @RequestBody @Valid MemberRegister.Request request
            ) {

        return memberServiceImpl.register(request);
    }
}
