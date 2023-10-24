package com.jejoonlee.movmag.app.member.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String accessToken;
    private String tokenType;

}
