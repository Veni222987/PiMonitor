package org.pi.server.utils;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.pi.server.common.Result;
import org.pi.server.common.ResultCode;
import org.pi.server.common.ResultUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {

    // 令牌JWT 存储的请求头
    public static String tokenHeader;
    // 签名秘钥
    private static String secret;
    // 令牌过期时间
    private static long expire;
    // JWT 的开头
    public static String tokenHead;

    @Value("${jwt.tokenHeader}")
    public void setTokenHeader(String tokenHeader) {
        JwtUtils.tokenHeader = tokenHeader;
    }
    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        JwtUtils.secret = secret;
    }
    @Value("${jwt.expire}")
    public void setExpire(long expire) {
        JwtUtils.expire = expire;
    }
    @Value("${jwt.tokenHead}")
    public void setTokenHead(String tokenHead) {
        JwtUtils.tokenHead = tokenHead;
    }


    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // 设置 JWT 的类型 (默认 JWT)
                .setHeaderParam("alg", "HS256") // 设置 JWT 的签名算法 （默认 HS256)
                .addClaims(claims) // 设置 JWT 的第二部分负载 payload 中存储的内容
                .signWith(SignatureAlgorithm.HS256, secret) // 使用 HS256 签名算法对 JWT 进行签名
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000L)) // 设置 JWT 的过期时间
                .compact();
    }

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @param expire 过期时间 单位秒
     * @return
     */
    public static String generateJwt(Map<String, Object> claims, Long expire) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT") // 设置 JWT 的类型 (默认 JWT)
                .setHeaderParam("alg", "HS256") // 设置 JWT 的签名算法 （默认 HS256)
                .addClaims(claims) // 设置 JWT 的第二部分负载 payload 中存储的内容
                .signWith(SignatureAlgorithm.HS256, secret) // 使用 HS256 签名算法对 JWT 进行签名
                .setExpiration(new Date(System.currentTimeMillis() + expire * 1000L)) // 设置 JWT 的过期时间
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
                .setSigningKey(secret) // 设置签名秘钥(会根据秘钥推断签名算法)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public static boolean parseJWT(String jwt, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            Claims claims = JwtUtils.parseJWT(jwt);
            claims.forEach(request::setAttribute);
        } catch (ExpiredJwtException e) {
            Result<Object> error = ResultUtils.error(ResultCode.TOKEN_EXPIRED);
            String result = JSONObject.toJSONString(error);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(result);
            return false;
        } catch (Exception e) {
            log.warn(e.getMessage());
            Result<Object> error = ResultUtils.error(ResultCode.PARAMS_ERROR);
            String result = JSONObject.toJSONString(error);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(result);
            return false;
        }
        return true;
    }
}
