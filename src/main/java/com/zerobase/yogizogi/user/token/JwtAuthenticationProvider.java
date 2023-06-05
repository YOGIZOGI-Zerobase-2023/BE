package com.zerobase.yogizogi.user.token;

import com.zerobase.yogizogi.user.util.Aes256Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Objects;
import com.zerobase.yogizogi.user.common.UserType;
public class JwtAuthenticationProvider {

    private final String secretKey = "secretKey";
    private final long tokenValidTime = 1000L * 60 * 60 * 24; //인증 만료 하루

    public String createToken(String email, Long id, UserType userType) {
        Claims claims = Jwts.claims()
            .setSubject(Aes256Utils.encrypt(email))
            .setId(Aes256Utils.encrypt(id.toString()));
        claims.put("roles", userType);
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
        return new UserDto(
            Long.valueOf(Objects.requireNonNull(Aes256Utils.decrypt(claims.getId()))),
            Aes256Utils.decrypt(claims.getSubject())
        );
    }
}
