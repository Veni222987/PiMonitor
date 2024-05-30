package org.pi.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {

    private static final String signKey = "koicmetkciwue^%3847JI&#HJ(*FD^AH#(ASY&H#E)A&#HA(#HJAL)*@#789q3U(*E(3990"; // 签名秘钥

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return
     */
    public static String generateJwt(Map<String, Object> claims) {
        // 过期时间24h
        long expire = 24 * 3600 * 1000L;
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey) // 使用 HS256 签名算法对 JWT 进行签名
                .setExpiration(new Date(System.currentTimeMillis() + expire)) // 设置 JWT 的过期时间
                .compact();
    }

    public static String generateJwt(Map<String, Object> claims, Long expire) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey) // 使用 HS256 签名算法对 JWT 进行签名
                .setExpiration(new Date(System.currentTimeMillis() + expire)) // 设置 JWT 的过期时间
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     * @throws ExpiredJwtException 如果 JWT已过期
     */
    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
