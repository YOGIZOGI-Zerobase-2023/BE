package com.zerobase.yogizogi.user.token;

import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtAuthenticationProvider {

    private static final String secretKey = "secretKey";

    public String createToken(String email, Long id, String nickName) {
        Claims claims = Jwts.claims()
            .setSubject(email).setIssuer(nickName)
            .setId(id.toString());

        Date now = new Date();
        long tokenValidTime = 1000L * 60 * 60 * 24;
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public UserDto getUserDto(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();

        String id = claims.getId();
        if (id == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        return new UserDto(Long.valueOf(id), claims.getSubject());
    }
}