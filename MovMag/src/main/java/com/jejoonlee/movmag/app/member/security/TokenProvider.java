package com.jejoonlee.movmag.app.member.security;

import com.jejoonlee.movmag.app.member.dto.TokenDto;
import com.jejoonlee.movmag.app.member.service.impl.MemberServiceImpl;
import com.jejoonlee.movmag.redis.RedisDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final MemberServiceImpl memberServiceImpl;
    private final RedisDao redisDao;
    private static final String KEY_ROLES = "role";
    private static final String EMAIL = "email";
    private static final long ACCESS_TOKEN_VALIDATE_TIME = 1 * 60 * 60 * 1000L; // 1시간

    private static final long REFRESH_TOKEN_VALIDATE_TIME =  7 * 24 * 60 * 60 * 1000L; // 1주일

    @Value("${spring.jwt.secret.key}")
    private String secretKey;

    public TokenDto.TokenInfo loginGenerateTokens(Long memberId, String email, String role) {

        String accessToken = generateToken(memberId, email, role, ACCESS_TOKEN_VALIDATE_TIME);
        String refreshToken = generateToken(memberId, email, role, REFRESH_TOKEN_VALIDATE_TIME);

        // redis에 저장
        redisDao.setValues(String.valueOf(memberId),
                refreshToken,
                Duration.ofMillis(REFRESH_TOKEN_VALIDATE_TIME));

        return TokenDto.TokenInfo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Access 토큰 생성 매서드
    public String generateToken(Long memberId, String email, String role, long time) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        claims.put(EMAIL, email);
        claims.put(KEY_ROLES, role); // key value로 저장

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + time);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = memberServiceImpl.loadUserByUsername(getMemberId(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getMemberId(String token) {
        return this.parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        // 토큰이 빈 문자열이면 false
        if (!StringUtils.hasText(token)) return false;

        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date()); //토큰 만료 시간이 현재보다 이전인지 아닌지 만료 여부 확인
    }

    // 토큰이 유효한지 확인하는 메서드
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            // 파싱하는 과정에서 토큰 만료 시간이 지날 수 있다, 만료된 토큰을 확인할 때에
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
