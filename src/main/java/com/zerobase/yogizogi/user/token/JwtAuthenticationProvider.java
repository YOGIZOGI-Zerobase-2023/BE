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

    private static final String secretKey = "secretKey"; // 계속 쓰이는 변수는 전역변수로 관리하자!(값이 있는 해당 형태만 static final 가능!)

    public String createToken(String email, Long id,String nickName) {
        Claims claims = Jwts.claims()
            .setSubject(email).setIssuer(nickName)
            .setId(id.toString());

        Date now = new Date();
        //인증 만료 하루
        long tokenValidTime = 1000L * 60 * 60 * 24;
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            //시간 만료되었는지로 검증
         Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
         return claims.getBody().getExpiration().before(new Date()); //true 만료 . false 만료가 아님
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