package com.jejoonlee.movmag.app.member.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "member")
@EntityListeners(value = AuditingEntityListener.class)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long memberId;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="username", nullable = false, length = 50)
    private String username;

    @Column(name="password", nullable = false, length = 1000)
    private String password;

    @Column(name="phone_number", nullable = false, length = 20)
    private String phoneNum;

    @Column(name="role", nullable = false, length = 10)
    private String role;

    @Column(name="registered_at", nullable = false)
    @CreatedDate
    private LocalDateTime registeredAt;

    @Column(name="updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
