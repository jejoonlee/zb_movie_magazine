package com.jejoonlee.movmag.app.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    USER("User"),
    EDITOR("Editor"),
    ADMIN("Admin");

    private String value;
}
