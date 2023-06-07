package com.zerobase.yogizogi.user.token;

import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.common.UserRole;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.util.Aes256Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtAuthenticationProvider {

    private final String secretKey = "secretKey";
    private final long tokenValidTime = 1000L * 60 * 60 * 24; //인증 만료 하루

    public String createToken(String email, Long id, UserRole userRole) {
        Claims claims = Jwts.claims()
            .setSubject(Aes256Utils.encrypt(email))
            .setId(Aes256Utils.encrypt(id.toString()));
        claims.put("roles", userRole);
        Date now = new Date();
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtToken);
            return !claimsJws.getBody()
                .getExpiration()
                .before(new Date());
        } catch (Exception e) {
            return false;
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

        String decryptedId = Aes256Utils.decrypt(id);
        if (decryptedId == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        return new UserDto(
            Long.valueOf(decryptedId),
            Aes256Utils.decrypt(claims.getSubject())
        );
    }
}