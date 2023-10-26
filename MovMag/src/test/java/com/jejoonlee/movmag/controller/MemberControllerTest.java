package com.jejoonlee.movmag.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jejoonlee.movmag.app.member.controller.MemberController;
import com.jejoonlee.movmag.app.member.dto.MemberLogin;
import com.jejoonlee.movmag.app.member.dto.MemberRegister;
import com.jejoonlee.movmag.app.member.security.TokenProvider;
import com.jejoonlee.movmag.app.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String EMAIL = "joons@naver.com";
    private String USERNAME = "joons";
    private String PASSWORD = "joons1234!";
    private String PHONE_NUM = "010-1234-1234";
    private String ROLE = "User";

    @Test
    @DisplayName("회원가입 성공 테스트")
    @WithMockUser
    void successRegisterMember() throws Exception {
    //given

        MemberRegister.Request request = MemberRegister.Request.builder()
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .phoneNum(PHONE_NUM)
                .role(ROLE)
                .build();

        MemberRegister.Response response = MemberRegister.Response.builder()
                .email(EMAIL)
                .username(USERNAME)
                .role(ROLE)
                .build();

        given(memberService.register(any()))
                .willReturn(response);
    //when
    //then
        mockMvc.perform(post("/member/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.role").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    @WithMockUser
    void successLogin() throws Exception {
        //given

        MemberLogin.Request request = MemberLogin.Request.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        MemberLogin.Response response = MemberLogin.Response.builder()
                .email(EMAIL)
                .memberId(1L)
                .role(ROLE)
                .build();

        given(memberService.login(any()))
                .willReturn(response);
        //when
        //then
        mockMvc.perform(post("/member/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
