package com.jejoonlee.movmag.app.member.service;

import com.jejoonlee.movmag.app.member.dto.MemberLogin;
import com.jejoonlee.movmag.app.member.dto.MemberRegister;

public interface MemberService {
    MemberRegister.Response register(MemberRegister.Request request);

    MemberLogin.Response login(MemberLogin.Request request);

}
